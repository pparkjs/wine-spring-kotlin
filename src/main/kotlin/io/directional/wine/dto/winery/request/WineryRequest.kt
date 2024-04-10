package io.directional.wine.dto.winery.request

data class WineryCreateRequest(
    val korName:String,
    val engName:String,
    val regionId:Long,
)

data class WineryUpdateRequest(
    val korName:String,
    val engName:String,
)
