package com.eternality.controller

import com.eternality.domain.UserEntity
import com.eternality.dto.AuthRequest
import com.eternality.dto.CreateUserRequest
import com.eternality.dto.UserView
import com.eternality.mapper.mapToUser
import com.eternality.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.stream.Collectors
import javax.validation.Valid

@RestController
@RequestMapping(path = ["api/public"])
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtEncoder: JwtEncoder,
    private val userService: UserService,
) {

    @PostMapping("login")
    fun login(@RequestBody @Valid request: AuthRequest): ResponseEntity<UserView> {
        try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.userName, request.password)
            )
            val user: UserEntity = authentication.principal as UserEntity
            val now = Instant.now()
            val expiry = 36000L
            val scope = authentication.authorities.joinToString(" ") { it.authority }

            val claims = JwtClaimsSet.builder()
                .issuer("example.io")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                //.subject(String.format("%s,%s", user.userId, user.username))
                .subject(String.format("%s,%s", user.userId, user.username))
                .claim("roles", scope)
                .build()

            val token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue

            return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(user.mapToUser())

        } catch (ex: BadCredentialsException) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build()
        }
    }

    @PostMapping("register")
    fun register(@RequestBody @Valid request: CreateUserRequest): UserView =
        userService.createUser(request)
}