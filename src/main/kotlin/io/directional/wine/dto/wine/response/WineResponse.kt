package io.directional.wine.dto.wine.response

import io.directional.wine.domain.region.Region
import io.directional.wine.domain.vo.Name
import io.directional.wine.domain.wine.Wine
import io.directional.wine.domain.wine.WineType
import io.directional.wine.domain.winegrape.WineGrape
import io.directional.wine.dto.region.response.RegionParentResponse

data class WineCreateResponse(
    val id: Long,
){
    companion object{
        fun of(wine: Wine) = WineCreateResponse(id = wine.id)
    }
}

data class WineListResponse(
    val id:Long,
    val type:WineType,
    val korName:String,
    val engName:String,
    val topLevelKorRegionName:String,
    val topLevelEngRegionName:String,
)

data class WineDetailResponse(
    val id:Long,
    val type:WineType,
    val korName:String,
    val engName:String,
    val alcohol:Double,
    val acidity:Int,
    val body:Int,
    val sweetness:Int,
    val tannin:Int,
    val score:Double,
    val price:Int,
    val style:String?,
    val grade:String?,
    val importer:String,
    val wineryKorName:String,
    val wineryEngName:String,
    val wineryRegionKorName:String,
    val wineryRegionEngName:String,
    val regionKorName:String,
    val regionEngName:String,
    val parentRegions: List<RegionParentResponse>,
    val grapes: List<WineGrapeResponse>,
    val aromas: List<String>,
    val pairings: List<String>,
){
    companion object{
        fun of(wine:Wine, parentRegions: List<Region>)=
            WineDetailResponse(
                id = wine.id,
                type = wine.type,
                korName = wine.name.korName,
                engName = wine.name.engName,
                alcohol = wine.alcohol,
                acidity = wine.acidity,
                body = wine.body,
                sweetness = wine.sweetness,
                tannin = wine.tannin,
                score = wine.score,
                price = wine.price,
                style = wine.style,
                grade = wine.grade,
                importer = wine.importer,
                wineryKorName = wine.winery.name.korName,
                wineryEngName = wine.winery.name.engName,
                wineryRegionKorName = wine.winery.region.name.korName,
                wineryRegionEngName = wine.winery.region.name.engName,
                regionKorName = wine.region.name.korName,
                regionEngName = wine.region.name.engName,
                parentRegions = RegionParentResponse.toList(parentRegions),
                grapes = WineGrapeResponse.toList(wine.wineGrape),
                aromas = wine.wineAroma.map{it.aroma},
                pairings = wine.winePairing.map{it.pairing},
            )
    }
}

data class WineGrapeResponse(
    val korName:String,
    val engName:String,
){
    companion object {
        fun toList(wineGrapes: List<WineGrape>): List<WineGrapeResponse> =
            wineGrapes.map(WineGrapeResponse::of)

        fun of(wineGrape:WineGrape)=
            WineGrapeResponse(
                korName = wineGrape.grape.name.korName,
                engName = wineGrape.grape.name.engName,
            )
    }
}

data class ImporterDetailResponse(
    val importerName:String,
    val wineNames:List<WineImporterResponse>
){
    companion object{
        fun of(importerName: String, wineNames: List<Name>): ImporterDetailResponse =
            ImporterDetailResponse(
                importerName = importerName,
                wineNames = WineImporterResponse.toList(wineNames)
            )
    }
}

data class WineImporterResponse(
    val korName:String,
    val engName:String,
){
    companion object{
        fun toList(wineNames: List<Name>): List<WineImporterResponse> =
            wineNames.map(WineImporterResponse::of)

        fun of(name:Name):WineImporterResponse =
            WineImporterResponse(
                korName = name.korName,
                engName = name.engName,
            )
    }
}
