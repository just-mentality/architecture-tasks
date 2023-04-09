package com.eternality.dto

import com.eternality.enums.ErrorCode

data class ErrorDescription(val errorCode: ErrorCode, val userMessage: String)