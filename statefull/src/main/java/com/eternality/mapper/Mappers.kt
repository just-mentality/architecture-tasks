package com.eternality.mapper

import com.eternality.domain.UserEntity
import com.eternality.dto.CreateUserRequest
import com.eternality.dto.User

fun CreateUserRequest.mapToUserEntity(): UserEntity =
    UserEntity().also {
        it.userName = this.userName
        it.firstName = this.firstName
        it.lastName = this.lastName
        it.email = this.email
        it.phone = this.phone
    }

fun UserEntity.mapToUser(): User =
    User(
        userId = userId!!,
        userName = userName,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone
    )