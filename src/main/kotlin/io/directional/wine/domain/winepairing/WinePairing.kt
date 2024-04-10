package io.directional.wine.domain.winepairing

import io.directional.wine.domain.BaseEntity
import io.directional.wine.domain.wine.Wine
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "wine_pairing")
class WinePairing(
    wine: Wine?,
    pairing: String,
) : BaseEntity(){

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_id")
    var wine: Wine? = wine
        protected set

    @Column(nullable = false)
    var pairing: String = pairing
        protected set

    companion object {
        fun createWinePairing(pairing: String, wine: Wine?):WinePairing =
            WinePairing(pairing = pairing, wine = wine)

    }
    fun updateWine(wine: Wine) {
        this.wine = wine
    }
}
