package io.directional.wine.dto.region.response

import io.directional.wine.domain.grapeshare.GrapeShare
import io.directional.wine.domain.region.Region
import io.directional.wine.domain.wine.Wine
import io.directional.wine.domain.winery.Winery

data class RegionCreateResponse(
    val id: Long,
    val parentId: Long?,
    val depth: Int,
    val rootId: Long?,
    val korName: String,
    val engName: String,
) {
    companion object{
        fun of(region: Region) = RegionCreateResponse(
            id = region.id,
            parentId = region.parent?.id,
            depth = region.depth,
            rootId = region.rootId,
            korName = region.name.korName,
            engName = region.name.engName,
        )
    }
}

data class RegionListResponse(
    val id: Long,
    val korName: String,
    val engName: String,
){
    companion object{
        fun of(region: Region) = RegionListResponse(
            id = region.id,
            korName = region.name.korName,
            engName = region.name.engName,
        )
    }
}

data class RegionDetailResponse(
    val id: Long,
    val korName: String,
    val engName: String,
    val parentRegions: List<RegionParentResponse>,
    val grapes: List<RegionGrapeResponse>,
    val wineries: List<RegionWineryResponse>,
    val wines: List<RegionWineResponse>,
){
    companion object{
        fun of(
                region: Region,
                parentRegions: List<Region>,
                grapes: List<GrapeShare>,
                wineries: List<Winery>,
                wines: List<Wine>,
            ) =
                RegionDetailResponse(
                    id = region.id,
                    korName = region.name.korName,
                    engName = region.name.engName,
                    parentRegions = RegionParentResponse.toList(parentRegions),
                    grapes = RegionGrapeResponse.toList(grapes),
                    wineries = RegionWineryResponse.toList(wineries),
                    wines = RegionWineResponse.toList(wines),
                )
    }
}

data class RegionParentResponse(
    val korName:String,
    val engName:String,
    val depth:Int,
){
    companion object {
        fun toList(parentRegions: List<Region>): List<RegionParentResponse> =
            parentRegions.map(RegionParentResponse::of)

        fun of(region:Region):RegionParentResponse =
            RegionParentResponse(
                korName = region.name.korName,
                engName = region.name.engName,
                depth = region.depth,
            )
    }

}

data class RegionGrapeResponse(
    val korName:String?,
    val engName:String?,
){
    companion object {
        fun toList(grapes: List<GrapeShare>): List<RegionGrapeResponse> =
            grapes.map(RegionGrapeResponse::of)

        fun of(grapeShare:GrapeShare): RegionGrapeResponse =
            RegionGrapeResponse(
                korName = grapeShare.grape?.name?.korName,
                engName = grapeShare.grape?.name?.engName
            )
    }

}

data class RegionWineryResponse(
    val korName:String,
    val engName:String,
){
    companion object {
        fun toList(wineries: List<Winery>): List<RegionWineryResponse> =
            wineries.map(RegionWineryResponse::of)

        fun of(winery:Winery): RegionWineryResponse =
            RegionWineryResponse(
                korName = winery.name.korName,
                engName = winery.name.engName
            )
    }
}

data class RegionWineResponse(
    val korName:String,
    val engName:String,
){
    companion object {
        fun toList(wines: List<Wine>): List<RegionWineResponse> =
            wines.map(RegionWineResponse::of)

        fun of(wine:Wine): RegionWineResponse =
            RegionWineResponse(
                korName = wine.name.korName,
                engName = wine.name.engName
            )
    }
}
