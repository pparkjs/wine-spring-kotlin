package io.directional.wine.domain.grapeshare

import io.directional.wine.domain.BaseEntity
import io.directional.wine.domain.grape.Grape
import io.directional.wine.domain.region.Region
import jakarta.persistence.*

@Entity
@Table(name = "grape_share")
class GrapeShare(
    share:Double,
    region:Region,
    grape: Grape?,
) : BaseEntity(){

    @Column(name = "share", nullable = false)
    var share: Double = share
        protected set
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    var region: Region = region
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grape_id")
    var grape: Grape? = grape
        protected set

    companion object{
        fun createGrapeShare(
            share:Double,
            region:Region,
            grape: Grape?,
        ):GrapeShare =
            GrapeShare(share, region, grape)
    }

    fun updateGrape(grape: Grape) {
        this.grape = grape
    }

}
