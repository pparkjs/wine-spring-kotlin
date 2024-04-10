package io.directional.wine.domain.wine.repository

import io.directional.wine.domain.region.Region
import io.directional.wine.domain.vo.Name
import io.directional.wine.domain.wine.Wine
import io.directional.wine.domain.winery.Winery
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface WineRepository : JpaRepository<Wine, Long>, CustomWineRepository {
    fun deleteByRegion(region: Region)

    fun deleteByWinery(winery: Winery)

    @Query("select w.name from Wine w where w.winery =:winery")
    fun findByWinery(winery: Winery?): List<Name>

    @Query("select w.name from Wine w where w.importer =:importerName")
    fun findByImporter(importerName: String): List<Name>

}
