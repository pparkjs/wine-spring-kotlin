package io.directional.wine.domain.grape

import io.directional.wine.domain.BaseEntity
import io.directional.wine.domain.grapeshare.GrapeShare
import io.directional.wine.domain.vo.Name
import jakarta.persistence.*

@Entity
@Table(name = "grape")
class Grape(
    name: Name,
    acidity:Int,
    body:Int,
    sweetness:Int,
    tannin:Int,
) : BaseEntity(){
    @Embedded
    var name: Name = name
        protected set
    @Column(name = "acidity", nullable = false)
    var acidity: Int = acidity
        protected set
    @Column(name = "body", nullable = false)
    var body: Int = body
        protected set
    @Column(name = "sweetness", nullable = false)
    var sweetness: Int = sweetness
        protected set
    @Column(name = "tannin", nullable = false)
    var tannin: Int = tannin
        protected set

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "grape")
    protected val mutableGrapeShares: MutableList<GrapeShare> = mutableListOf()
    val grapeShares: List<GrapeShare> get() = mutableGrapeShares.toList()

    companion object{
        fun createGrape(
            korName: String,
            engName: String,
            acidity: Int,
            body: Int,
            sweetness: Int,
            tannin: Int,
            grapeShares: List<GrapeShare>
        ):Grape =
            Grape(Name(korName, engName), acidity, body, sweetness, tannin).apply {
                grapeShares.forEach(this::addGrapeShare)
            }
    }

    fun updateGrape(
        korName: String,
        engName: String,
        acidity: Int,
        body: Int,
        sweetness: Int,
        tannin: Int,
    ) {
        this.name = Name(korName, engName)
        this.acidity = acidity
        this.body = body
        this.sweetness = sweetness
        this.tannin = tannin
    }

   // 연관관계 편의 메소드
    fun addGrapeShare(grapeShare: GrapeShare) {
        mutableGrapeShares.add(grapeShare)
        grapeShare.updateGrape(this)
    }

}
