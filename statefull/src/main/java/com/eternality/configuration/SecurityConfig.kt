package com.eternality.configuration

import com.eternality.domain.repository.UserRepository
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
class SecurityConfig(
    private val userRepository: UserRepository
) {

    @Value("\${springdoc.api-docs.path}")
    private lateinit var restApiDocPath: String

    @Value("\${springdoc.swagger-ui.path}")
    private lateinit var swaggerPath: String

    @Value("\${jwt.public.key}")
    private lateinit var rsaPublicKey: RSAPublicKey

    @Value("\${jwt.private.key}")
    private lateinit var rsaPrivateKey: RSAPrivateKey

    @Bean
    fun authenticationManager(http: HttpSecurity, bCryptPasswordEncoder: BCryptPasswordEncoder): AuthenticationManager =
        http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .userDetailsService(
                UserDetailsService { username: String ->
                    userRepository
                        .findByUserName(username)
                        .orElseThrow { UsernameNotFoundException("User: $username, not found") }
                })
            .passwordEncoder(bCryptPasswordEncoder)
            .and()
            .build()


    // Used by JwtAuthenticationProvider to generate JWT tokens
    @Bean
    fun jwtEncoder(): JwtEncoder {
        val jwk: RSAKey = RSAKey.Builder(this.rsaPublicKey).privateKey(this.rsaPrivateKey).build()
        val jwks = ImmutableJWKSet<SecurityContext>(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }

    // Used by JwtAuthenticationProvider to decode and validate JWT tokens
    @Bean
    fun jwtDecoder(): JwtDecoder =
        NimbusJwtDecoder.withPublicKey(rsaPublicKey).build()

    // Extract authorities from the roles claim
    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter =
        JwtAuthenticationConverter().apply {
            this.setJwtGrantedAuthoritiesConverter(
                JwtGrantedAuthoritiesConverter().also {
                    it.setAuthoritiesClaimName("roles")
                    it.setAuthorityPrefix("ROLE_")
                })
        }

    // Set password encoding schema
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    // Used by spring security if CORS is enabled.
    @Bean
    fun corsFilter(): CorsFilter {
        val config = CorsConfiguration().also {
            it.allowCredentials = true
            it.addAllowedOrigin("*")
            it.addAllowedHeader("*")
            it.addAllowedMethod("*")
        }
        val source = UrlBasedCorsConfigurationSource().also {
            it.registerCorsConfiguration("/**", config)
        }
        return CorsFilter(source)
    }

    // Expose authentication manager bean
    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // Enable CORS and disable CSRF
        http.cors().and().csrf().disable()

        // Set session management to stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // Set unauthorized requests exception handler
        http.exceptionHandling { exceptions: ExceptionHandlingConfigurer<HttpSecurity> ->
            exceptions
                .authenticationEntryPoint(BearerTokenAuthenticationEntryPoint())
                .accessDeniedHandler(BearerTokenAccessDeniedHandler())
        }

        // Set permissions on endpoints
        http.authorizeRequests() // Swagger endpoints must be publicly accessible
            .antMatchers("/")
            .permitAll()
            .antMatchers(String.format("%s/**", restApiDocPath))
            .permitAll()
            .antMatchers(String.format("%s/**", swaggerPath))
            .permitAll() // Our public endpoints
            .antMatchers("/api/public/**")
            .permitAll()
            .antMatchers(HttpMethod.GET, "/api/author/**")
            .permitAll()
            .antMatchers(HttpMethod.POST, "/api/author/search")
            .permitAll()
            .antMatchers(HttpMethod.GET, "/api/book/**")
            .permitAll()
            .antMatchers(HttpMethod.POST, "/api/book/search")
            .permitAll() // Our private endpoints
            .anyRequest()
            .authenticated() // Set up oauth2 resource server
            .and()
            .httpBasic(Customizer.withDefaults())
            .oauth2ResourceServer { it.jwt() }

        return http.build()
    }

}