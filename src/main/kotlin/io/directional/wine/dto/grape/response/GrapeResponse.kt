package io.directional.wine.dto.grape.response

import io.directional.wine.domain.grape.Grape
import io.directional.wine.domain.grapeshare.GrapeShare
import io.directional.wine.domain.region.Region
import io.directional.wine.domain.winegrape.WineGrape

data class GrapeCreateResponse(
    val id: Long,
) {
    companion object{
        fun of(grape: Grape) = GrapeCreateResponse(id = grape.id)
    }
}

data class GrapeListResponse(
    val id: Long,
    val korName: String,
    val engName: String,
    val regions: List<GrapeRegionResponse>
){
    companion object{
        fun of(grape: Grape) =
            GrapeListResponse(
                id = grape.id,
                korName = grape.name.korName,
                engName = grape.name.engName,
                regions = GrapeRegionResponse.toList(grape.grapeShares)
            )
    }
}

data class GrapeRegionResponse(
    val korName:String,
    val engName:String,
){
    companion object{
        fun toList(grapeShares: List<GrapeShare>) =
            grapeShares.map{
                grapeShares -> of(grapeShares.region)
            }

        fun of(region: Region) =
            GrapeRegionResponse(
                korName = region.name.korName,
                engName = region.name.engName
            )
    }
}

data class GrapeDetailResponse(
    val id: Long,
    val grapeKorName: String,
    val grapeEngName: String,
    val acidity: Int,
    val body: Int,
    val sweetness: Int,
    val tannin: Int,
    val regions: List<GrapeRegionResponse>,
    val wines: List<GrapeWineResponse>,

){
    companion object{
        fun of(grape: Grape, wineGrapes: List<WineGrape>) =
            GrapeDetailResponse(
                id = grape.id,
                grapeKorName = grape.name.korName,
                grapeEngName = grape.name.engName,
                acidity = grape.acidity,
                body = grape.body,
                sweetness = grape.sweetness,
                tannin = grape.tannin,
                regions = GrapeRegionResponse.toList(grape.grapeShares),
                wines = GrapeWineResponse.toList(wineGrapes),
            )
    }
}

data class GrapeWineResponse(
    val korName:String?,
    val engName:String?,
){
    companion object{
        fun toList(wineGrapes: List<WineGrape>): List<GrapeWineResponse> =
            wineGrapes.map(GrapeWineResponse::of)

        fun of(wineGrape: WineGrape) =
            GrapeWineResponse(
                korName = wineGrape.wine?.name?.korName,
                engName = wineGrape.wine?.name?.engName
            )
    }
}
