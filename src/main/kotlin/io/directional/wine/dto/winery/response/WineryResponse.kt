package io.directional.wine.dto.winery.response

import io.directional.wine.domain.vo.Name
import io.directional.wine.domain.winery.Winery

data class WineryCreateResponse(
    val id: Long,
    val korName: String,
    val engName: String,
    val regionKorName: String,
    val regionEngName: String,
) {
    companion object{
        fun of(winery: Winery) = WineryCreateResponse(
            id = winery.id,
            korName = winery.name.korName,
            engName = winery.name.engName,
            regionKorName = winery.region.name.korName,
            regionEngName = winery.region.name.engName,
        )
    }
}

data class WineryListResponse(
    val id: Long,
    val korName: String,
    val engName: String,
    val regionKorName: String,
    val regionEngName: String,
){
    companion object{
        fun of(winery: Winery) = WineryListResponse(
            id = winery.id,
            korName = winery.name.korName,
            engName = winery.name.engName,
            regionKorName = winery.region.name.korName,
            regionEngName = winery.region.name.engName,
        )
    }
}

data class WineryDetailResponse(
    val id: Long,
    val korName: String,
    val engName: String,
    val regionKorName: String,
    val regionEngName: String,
    val wines: List<WineryWineResponse>
){
    companion object{
        fun of(winery: Winery, wineNames: List<Name>): WineryDetailResponse =
            WineryDetailResponse(
                id = winery.id,
                korName = winery.name.korName,
                engName = winery.name.engName,
                regionKorName = winery.region.name.korName,
                regionEngName = winery.region.name.engName,
                wines = WineryWineResponse.toList(wineNames)
            )

    }
}

data class WineryWineResponse(
    val korName: String,
    val engName: String,
){
    companion object{
        fun toList(wineNames: List<Name>): List<WineryWineResponse> =
            wineNames.map(WineryWineResponse::of)

        fun of(name:Name): WineryWineResponse =
            WineryWineResponse(
                korName = name.korName,
                engName = name.engName,
            )
    }
}
