package io.directional.wine.service.region

import io.directional.wine.common.dto.ListPageResponse
import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.dto.region.response.RegionCreateResponse
import io.directional.wine.dto.region.response.RegionDetailResponse
import io.directional.wine.dto.region.response.RegionListResponse
import org.springframework.data.domain.Pageable

interface RegionService {
    fun insertRegion(korName: String, engName: String, parentId: Long?): ResultResponse<RegionCreateResponse>
    fun updateRegion(korName: String, engName: String, regionId: Long)
    fun deleteRegion(regionId: Long)
    fun getRegions(pageable: Pageable, keyword: String?, filter: Long?): ListPageResponse<RegionListResponse>
    fun getRegion(regionId: Long): ResultResponse<RegionDetailResponse>
}
