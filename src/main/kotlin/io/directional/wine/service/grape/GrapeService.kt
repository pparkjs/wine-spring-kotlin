package io.directional.wine.service.grape

import io.directional.wine.common.dto.ListPageResponse
import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.dto.grape.request.GrapeCreateRequest
import io.directional.wine.dto.grape.request.GrapeUpdateRequest
import io.directional.wine.dto.grape.response.GrapeCreateResponse
import io.directional.wine.dto.grape.response.GrapeDetailResponse
import io.directional.wine.dto.grape.response.GrapeListResponse
import org.springframework.data.domain.Pageable

interface GrapeService {
    fun insertGrape(request: GrapeCreateRequest): ResultResponse<GrapeCreateResponse>
    fun updateGrape(request: GrapeUpdateRequest, grapeId: Long)
    fun deleteGrape(grapeId: Long)
    fun getGrapes(pageable: Pageable, keyword: String?, filter: Long?, order: String, sort: String): ListPageResponse<GrapeListResponse>
    fun getGrape(grapeId: Long): ResultResponse<GrapeDetailResponse>
}
