package io.directional.wine.domain.wine

import io.directional.wine.domain.BaseEntity
import io.directional.wine.domain.region.Region
import io.directional.wine.domain.vo.Name
import io.directional.wine.domain.winearoma.WineAroma
import io.directional.wine.domain.winegrape.WineGrape
import io.directional.wine.domain.winepairing.WinePairing
import io.directional.wine.domain.winery.Winery
import jakarta.persistence.*

@Entity
@Table(name = "wine")
class Wine(
    name:Name,
    alcohol:Double,
    type:WineType,
    acidity:Int,
    body:Int,
    sweetness:Int,
    tannin:Int,
    servingTemperature:Double,
    score:Double,
    price:Int,
    style:String?,
    grade:String?,
    importer:String,
    region:Region,
    winery:Winery,
): BaseEntity() {
    @Embedded
    var name: Name = name
        protected set
    @Column(name = "type", nullable = false)
    @Convert(converter = WineTypeConverter::class)
    var type: WineType = type
        protected set
    @Column(name = "alcohol", nullable = false)
    var alcohol: Double = alcohol
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
    @Column(name = "serving_temperature", nullable = false)
    var servingTemperature: Double = servingTemperature
        protected set
    @Column(name = "score", nullable = false)
    var score: Double = score
        protected set
    @Column(name = "price", nullable = false)
    var price: Int = price
        protected set
    @Column(name = "style")
    var style: String? = style
        protected set
    @Column(name = "grade")
    var grade: String? = grade
        protected set
    @Column(name = "importer", nullable = false)
    var importer: String = importer
        protected set
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    var region: Region = region
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winery_id")
    var winery: Winery = winery
        protected set

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "wine",orphanRemoval = true)
    protected val mutableWineAroma: MutableList<WineAroma> = mutableListOf()
    val wineAroma: List<WineAroma> get() = mutableWineAroma.toList()

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "wine",orphanRemoval = true)
    protected val mutableWinePairing: MutableList<WinePairing> = mutableListOf()
    val winePairing: List<WinePairing> get() = mutableWinePairing.toList()

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "wine",orphanRemoval = true)
    protected val mutableWineGrape: MutableList<WineGrape> = mutableListOf()
    val wineGrape: List<WineGrape> get() = mutableWineGrape.toList()

    companion object {
        fun createWine(
            korName: String, engName: String, type: WineType,
            alcohol: Double, acidity: Int, body: Int,
            sweetness: Int, tannin: Int, servingTemperature: Double,
            score: Double, price: Int, style: String?,
            grade: String?, importer: String, winery: Winery,
            region: Region, wineGrape: List<WineGrape>, wineAroma: List<WineAroma>,
            winePairing: List<WinePairing>
        ): Wine =
            Wine(
                name = Name(korName, engName), type = type, alcohol = alcohol,
                acidity = acidity, body = body, sweetness = sweetness,
                tannin = tannin, servingTemperature = servingTemperature, score = score,
                price = price, style = style, grade = grade,
                importer = importer, winery = winery, region = region,
            ).apply {
                wineAroma.forEach(this::addWineAroma)
                winePairing.forEach(this::addWinePairing)
                wineGrape.forEach(this::addWineGrape)
            }
    }
    fun updateWine(
        korName: String, engName: String, type: WineType,
        alcohol: Double, acidity: Int, body: Int,
        sweetness: Int, tannin: Int, servingTemperature: Double,
        score: Double, price: Int, style: String?,
        grade: String?, importer: String, winery: Winery,
        region: Region
    ) {
        this.name = Name(korName, engName)
        this.type = type
        this.alcohol = alcohol
        this.acidity = acidity
        this.body = body
        this.sweetness = sweetness
        this.tannin = tannin
        this.servingTemperature = servingTemperature
        this.score = score
        this.price = price
        this.style = style
        this.grade = grade
        this.importer = importer
        this.winery = winery
        this.region = region
    }

    // 연관관계 편의 메소드
    fun addWineAroma(wineAroma: WineAroma) {
        mutableWineAroma.add(wineAroma)
        wineAroma.updateWine(this)
    }
    fun addWinePairing(winePairing: WinePairing) {
        mutableWinePairing.add(winePairing)
        winePairing.updateWine(this)
    }
    fun addWineGrape(wineGrape: WineGrape) {
        mutableWineGrape.add(wineGrape)
        wineGrape.updateWine(this)
    }


}
