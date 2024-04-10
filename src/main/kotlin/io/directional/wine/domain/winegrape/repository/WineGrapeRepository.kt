package io.directional.wine.domain.winegrape.repository

import io.directional.wine.domain.grape.Grape
import io.directional.wine.domain.winegrape.WineGrape
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface WineGrapeRepository : JpaRepository<WineGrape, Long> {

    @Modifying
    @Query("delete from WineGrape wg where wg.wine.id =:wineId")
    fun deleteByWineId(wineId: Long)

    fun deleteByGrape(grape: Grape)
    fun findByGrape(grape: Grape):List<WineGrape>
}
