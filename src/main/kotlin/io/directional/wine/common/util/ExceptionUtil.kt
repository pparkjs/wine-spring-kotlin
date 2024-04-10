package io.directional.wine.common.util

import io.directional.wine.common.exception.CustomException
import io.directional.wine.common.exception.ErrorCode
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

fun fail(): Nothing {
    throw CustomException(ErrorCode.RESOURCE_NOT_FOUND)
}

fun<T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T = this.findByIdOrNull(id)?: fail()
