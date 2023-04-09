package com.eternality.service

import com.eternality.domain.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CommonUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails =
        userRepository.findByUserName(username)
            .orElseThrow { UsernameNotFoundException("User with name=$username was not found!") }
}