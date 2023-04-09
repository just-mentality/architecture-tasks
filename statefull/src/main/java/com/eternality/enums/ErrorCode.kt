package com.eternality.enums

enum class ErrorCode(val userMessage: String) {
    UNAUTHORIZED("Пользователь не авторизован"),
    ACCESS_DENIED("Недостаточно прав для выполнения операции"),
    FORBIDDEN("Доступ к ресурсу запрещён");

    fun getCode(): String = this.name
}