package com.eternality.dto

import javax.validation.constraints.Email

data class AuthRequest(@Email val userName: String, val password: String)
