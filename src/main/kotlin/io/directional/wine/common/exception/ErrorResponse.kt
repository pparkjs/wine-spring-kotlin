package io.directional.wine.common.exception

import org.springframework.http.HttpStatus

data class ErrorResponse(
    val errorCode: String,
    val message: String?,
    val status: HttpStatus,
)
