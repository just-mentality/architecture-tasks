package com.eternality.exception

import com.eternality.exception.ExceptionResponse
import com.eternality.exception.ServiceException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class Advice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [ServiceException::class, RuntimeException::class])
    fun handleConflict(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(
            ex,
            ExceptionResponse(errorMessage = ex.message, status = ExceptionResponse.Status.FAILURE),
            HttpHeaders(),
            HttpStatus.BAD_REQUEST,
            request
        )
    }
}