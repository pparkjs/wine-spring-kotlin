package io.directional.wine.domain.grapeshare.repository

import io.directional.wine.domain.grapeshare.GrapeShare
import io.directional.wine.domain.region.Region
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface GrapeShareRepository : JpaRepository<GrapeShare, Long> {
    fun deleteByRegion(region: Region?)

    @Modifying
    @Query("delete from GrapeShare gs where gs.grape.id =:grapeId")
    fun deleteByGrapeId(grapeId: Long)
}
