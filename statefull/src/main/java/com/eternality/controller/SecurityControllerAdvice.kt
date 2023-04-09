package com.eternality.controller

import com.eternality.dto.ErrorDescription
import com.eternality.enums.ErrorCode
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class SecurityControllerAdvice {

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(request: HttpServletRequest): ResponseEntity<ErrorDescription> =
        ResponseEntity
            .status(HttpServletResponse.SC_FORBIDDEN)
            .body(
                ErrorDescription(
                    errorCode = ErrorCode.FORBIDDEN,
                    userMessage = String.format(
                        "%s: %s %s", ErrorCode.ACCESS_DENIED.userMessage, request.method, request.requestURI.toString()
                    )
                )
            )

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(request: HttpServletRequest): ResponseEntity<ErrorDescription> =
        ResponseEntity
            .status(HttpServletResponse.SC_UNAUTHORIZED)
            .body(
                ErrorDescription(
                    errorCode = ErrorCode.UNAUTHORIZED,
                    userMessage = String.format(
                        "%s: %s %s", ErrorCode.UNAUTHORIZED.userMessage, request.method, request.requestURI.toString()
                    )
                )
            )
}