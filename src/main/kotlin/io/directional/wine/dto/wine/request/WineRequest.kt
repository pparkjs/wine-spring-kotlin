package io.directional.wine.dto.wine.request

import io.directional.wine.domain.wine.WineType

data class WineCreateRequest(
    val korName:String,
    val engName:String,
    val type:WineType,
    val alcohol:Double,
    val acidity:Int,
    val body:Int,
    val sweetness:Int,
    val tannin:Int,
    val servingTemperature:Double,
    val score:Double,
    val price:Int,
    val style:String?,
    val grade:String?,
    val importer:String,
    val wineryId:Long,
    val regionId:Long,
    val aromas: List<String>,
    val pairings: List<String>,
    val grapes: List<Long>,
)

data class WineUpdateRequest(
    val korName:String,
    val engName:String,
    val type:WineType,
    val alcohol:Double,
    val acidity:Int,
    val body:Int,
    val sweetness:Int,
    val tannin:Int,
    val servingTemperature:Double,
    val score:Double,
    val price:Int,
    val style:String?,
    val grade:String?,
    val importer:String,
    val wineryId:Long,
    val regionId:Long,
    val aromas: List<String>,
    val pairings: List<String>,
    val grapes: List<Long>,
)

data class WineFilter(
    val wineType: Int?,
    val minAlcohol: Double?,
    val maxAlcohol: Double?,
    val minPrice: Int?,
    val maxPrice: Int?,
    val wineStyle: String?,
    val wineGrade: String?,
    val region: Long?
)
