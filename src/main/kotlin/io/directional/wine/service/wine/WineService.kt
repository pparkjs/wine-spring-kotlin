package io.directional.wine.service.wine

import io.directional.wine.common.dto.ListPageResponse
import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.dto.wine.request.WineCreateRequest
import io.directional.wine.dto.wine.request.WineFilter
import io.directional.wine.dto.wine.request.WineUpdateRequest
import io.directional.wine.dto.wine.response.ImporterDetailResponse
import io.directional.wine.dto.wine.response.WineCreateResponse
import io.directional.wine.dto.wine.response.WineDetailResponse
import io.directional.wine.dto.wine.response.WineListResponse
import org.springframework.data.domain.Pageable

interface WineService {

    fun insertWine(request: WineCreateRequest): ResultResponse<WineCreateResponse>
    fun updateWine(request: WineUpdateRequest, wineId: Long)
    fun deleteWine(wineId: Long)
    fun getWines(pageable: Pageable, keyword: String?, filter: WineFilter, order: String, sort: String): ListPageResponse<WineListResponse>
    fun getGrape(wineId: Long): ResultResponse<WineDetailResponse>
    fun getImporters(pageable: Pageable, keyword: String?): ListPageResponse<String>
    fun getImporter(importerName: String): ResultResponse<ImporterDetailResponse>

}
