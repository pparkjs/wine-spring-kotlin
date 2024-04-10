package io.directional.wine.common.dto

data class ListPageResponse<T>(
    val result: List<T> = listOf(),
    var totalCount: Long? = 0,
    var hasNext: Boolean,
)
