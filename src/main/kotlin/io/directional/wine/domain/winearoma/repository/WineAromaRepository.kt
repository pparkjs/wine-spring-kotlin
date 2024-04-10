package io.directional.wine.domain.winearoma.repository

import io.directional.wine.domain.winearoma.WineAroma
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface WineAromaRepository : JpaRepository<WineAroma, Long> {

    @Modifying
    @Query("delete from WineAroma wa where wa.wine.id =:wineId")
    fun deleteByWineId(wineId: Long)
}
