package com.eternality.dto

data class CreateUserRequest(
    val userName: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null
)
