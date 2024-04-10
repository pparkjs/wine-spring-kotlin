package io.directional.wine.common.util

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl

// 무한스크롤 페이징 위한 util
fun <T> checkEndPage(pageable: Pageable, results: List<T>): Slice<T> {
    var hasNext = false
    val modifiedResults = results.toMutableList()
    if (modifiedResults.size > pageable.pageSize) {
        hasNext = true
        modifiedResults.removeAt(pageable.pageSize)
    }
    return SliceImpl(modifiedResults, pageable, hasNext)
}
