package io.directional.wine.common.exception

class CustomException(
    val errorCode: ErrorCode
): RuntimeException()
