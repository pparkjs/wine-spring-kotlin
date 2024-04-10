package io.directional.wine.domain.region.repository

import io.directional.wine.domain.grapeshare.GrapeShare
import io.directional.wine.domain.region.Region
import io.directional.wine.domain.wine.Wine
import io.directional.wine.domain.winery.Winery
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CustomRegionRepository {
    fun getRegions(pageable: Pageable, keyword: String?, filter: Long?): Slice<Region>
    fun getRegionsCount(keyword: String?, filter: Long?): Long?
    fun getGrapeShareByRegion(regionId: Long): List<GrapeShare>
    fun getWineryByRegion(regionId: Long): List<Winery>
    fun getWineByRegion(regionId: Long): List<Wine>
    fun getAllParentRegion(regionId: Long): MutableList<Region>
}
