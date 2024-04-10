package io.directional.wine.domain.winegrape

import io.directional.wine.domain.BaseEntity
import io.directional.wine.domain.grape.Grape
import io.directional.wine.domain.wine.Wine
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "wine_grape")
class WineGrape(
    wine: Wine?,
    grape: Grape,
) : BaseEntity(){

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_id")
    var wine: Wine? = wine
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grape_id")
    var grape: Grape = grape
        protected set

    companion object {
        fun createWineGrape(grape: Grape, wine: Wine?):WineGrape =
            WineGrape(wine = wine, grape = grape)

    }

    fun updateWine(wine: Wine) {
        this.wine = wine
    }
}
