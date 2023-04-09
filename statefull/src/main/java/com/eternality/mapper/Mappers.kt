package com.eternality.mapper

import com.eternality.domain.UserEntity
import com.eternality.dto.CreateUserRequest
import com.eternality.dto.UserView
import org.springframework.security.crypto.password.PasswordEncoder

fun CreateUserRequest.mapToUserEntity(passwordEncoder: PasswordEncoder): UserEntity =
    UserEntity()
        .also {
            it.userName = this.userName
            it.pass = passwordEncoder.encode(this.password)
            it.firstName = this.firstName
            it.lastName = this.lastName
            it.email = this.email
            it.phone = this.phone
        }

fun UserEntity.mapToUser(): UserView =
    UserView(
        userId = userId!!,
        userName = userName,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone
    )