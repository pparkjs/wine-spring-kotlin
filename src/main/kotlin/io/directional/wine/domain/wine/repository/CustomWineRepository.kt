package io.directional.wine.domain.wine.repository

import io.directional.wine.dto.wine.request.WineFilter
import io.directional.wine.dto.wine.response.WineListResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CustomWineRepository {
    fun getWinesCount(keyword: String?, filter: WineFilter): Long?
    fun getWines(orderPageable: Pageable, keyword: String?, filter: WineFilter, order: String): Slice<WineListResponse>
    fun getImportersCount(keyword: String?): Long?
    fun getImporters(pageable: Pageable, keyword: String?): Slice<String>
}
