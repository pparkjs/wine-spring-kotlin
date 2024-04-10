package io.directional.wine.domain.winery

import io.directional.wine.domain.BaseEntity
import io.directional.wine.domain.region.Region
import io.directional.wine.domain.vo.Name
import jakarta.persistence.*

@Entity
@Table(name = "winery")
class Winery(
    name: Name,
    region: Region
):BaseEntity() {

    @Embedded
    var name: Name = name
        protected set
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    var region: Region = region
        protected set

    companion object{
        fun createWinery(
            korName: String,
            engName: String,
            region: Region,
        ) =
            Winery(
                name = Name(korName,engName),
                region = region,
            )
    }

    fun updateName(korName: String, engName: String) {
        name = Name(korName, engName)
    }
}
