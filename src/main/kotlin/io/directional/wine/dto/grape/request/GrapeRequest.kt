package io.directional.wine.dto.grape.request

data class GrapeCreateRequest(
    val korName:String,
    val engName:String,
    val acidity:Int,
    val body:Int,
    val sweetness:Int,
    val tannin:Int,
    val shares: List<ShareRequest>,
)

data class ShareRequest(
    val share:Double,
    val regionId:Long
)

data class GrapeUpdateRequest(
    val korName:String,
    val engName:String,
    val acidity:Int,
    val body:Int,
    val sweetness:Int,
    val tannin:Int,
    val shares: List<ShareRequest>,
)
