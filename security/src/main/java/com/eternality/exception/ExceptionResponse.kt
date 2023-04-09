package com.eternality.exception

data class ExceptionResponse(
    val errorMessage: String?= null,
    val status: Status? = Status.OK
) {
    enum class Status {
        OK, FAILURE
    }
}
