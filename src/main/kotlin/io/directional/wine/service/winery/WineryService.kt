package io.directional.wine.service.winery

import io.directional.wine.common.dto.ListPageResponse
import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.dto.winery.response.WineryCreateResponse
import io.directional.wine.dto.winery.response.WineryDetailResponse
import io.directional.wine.dto.winery.response.WineryListResponse
import org.springframework.data.domain.Pageable

interface WineryService {
    fun insertWinery(korName: String, engName: String, regionId: Long): ResultResponse<WineryCreateResponse>
    fun updateWinery(korName: String, engName: String, wineryId: Long)
    fun deleteWinery(wineryId: Long)
    fun getWineries(pageable: Pageable, keyword: String?, filter: Long?): ListPageResponse<WineryListResponse>
    fun getWinery(wineryId: Long): ResultResponse<WineryDetailResponse>
}
