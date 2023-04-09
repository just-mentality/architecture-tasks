package com.eternality.dto

data class UserView(
    var userId: Long,
    var userName: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var phone: String? = null
)