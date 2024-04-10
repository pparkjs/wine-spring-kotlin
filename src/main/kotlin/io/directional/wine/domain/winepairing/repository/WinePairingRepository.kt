package io.directional.wine.domain.winepairing.repository

import io.directional.wine.domain.winepairing.WinePairing
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface WinePairingRepository : JpaRepository<WinePairing, Long> {
    @Modifying
    @Query("delete from WinePairing wp where wp.wine.id =:wineId")
    fun deleteByWineId(wineId: Long)
}
