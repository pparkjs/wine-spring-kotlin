package io.directional.wine.domain.winearoma

import io.directional.wine.domain.BaseEntity
import io.directional.wine.domain.wine.Wine
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "wine_aroma")
class WineAroma(
    wine: Wine?,
    aroma: String,
) : BaseEntity(){

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_id")
    var wine: Wine? = wine
        protected set

    @Column(nullable = false)
    var aroma: String = aroma
        protected set

    companion object {
        fun createWineAroma(aroma: String, wine: Wine?):WineAroma =
            WineAroma(aroma = aroma, wine = wine)
    }

    fun updateWine(wine: Wine) {
        this.wine = wine
    }
}
