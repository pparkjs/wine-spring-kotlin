package io.directional.wine.common.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [CustomException::class])
    fun handlingCustomException(ex: CustomException): ResponseEntity<ErrorResponse> {
        val errorCode: ErrorCode = ex.errorCode
        val errorResponse = ErrorResponse(
            errorCode = errorCode.name,
            message = errorCode.message,
            status = errorCode.status,
        )
        return ResponseEntity(errorResponse, errorCode.status)
    }
}
