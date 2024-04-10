package io.directional.wine.domain.winery.repository

import io.directional.wine.domain.region.Region
import io.directional.wine.domain.winery.Winery
import org.springframework.data.jpa.repository.JpaRepository

interface WineryRepository : JpaRepository<Winery, Long>, CustomWineryRepository {

    fun deleteByRegion(region: Region)
}
