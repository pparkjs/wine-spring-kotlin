package io.directional.wine.dto.region.request

data class RegionCreateRequest(
    val korName:String,
    val engName:String,
    val parentId:Long? = null,
)

data class RegionUpdateRequest(
    val korName:String,
    val engName:String,
)
