package io.directional.wine.acceptance.wine

import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.domain.wine.WineType
import io.directional.wine.dto.region.request.RegionCreateRequest
import io.directional.wine.dto.region.request.RegionUpdateRequest
import io.directional.wine.dto.region.response.RegionDetailResponse
import io.directional.wine.dto.region.response.RegionParentResponse
import io.directional.wine.dto.wine.request.WineCreateRequest
import io.directional.wine.dto.wine.request.WineFilter
import io.directional.wine.dto.wine.request.WineUpdateRequest
import io.directional.wine.dto.wine.response.WineGrapeResponse
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When

abstract class WineAcceptanceTestHelper {

    companion object{
        fun 와인_저장_API_호출(request: WineCreateRequest) =
            Given {
                log().all()
                contentType(ContentType.JSON)
                body(request)
            } When {
                post("/wine")
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 와인_상세_조회_API_호출(wineId:Long) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                get("/wine/{wineId}", wineId)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 와인_수정_API_호출(request: WineUpdateRequest, wineId:Long) =
            Given {
                log().all()
                contentType(ContentType.JSON)
                body(request)
            } When {
                patch("/wine/{wineId}", wineId)
            } Then {
                log().all()
            } Extract {
                this
            }
        fun 와인_삭제_API_호출(wineId:Long) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                delete("/wine/{wineId}", wineId)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 와인_리스트_API_호출(filter:WineFilter, keyword:String, order:String, sort:String) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                get("/wine?page=0&size=5&keyword={keyword}&order={order}&sort={sort}" +
                        "&minPrice={minPrice}&maxPrice={maxPrice}&wineStyle={wineStyle}&region={region}"
                    , keyword, order, sort, filter.minPrice, filter.maxPrice, filter.wineStyle, filter.region)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 수입사_상세_조회_API_호출(importerName:String) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                get("/wine/importer/{importerName}", importerName)
            } Then {
                log().all()
            } Extract {
                this
            }
        fun 수입사_리스트_API_호출(keyword:String) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                get("/wine/importer?page=0&size=5&keyword={keyword}",keyword)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 와인_생성_요청_데이터(): WineCreateRequest =
            WineCreateRequest(
                korName = "알프레드 그라티엔, 클래식 브뤼",
                engName = "Alfred Gratien, Classic Brut",
                type = WineType.SPARKLING,
                alcohol = 12.5,
                acidity = 4,
                body = 1,
                sweetness = 1,
                tannin = 1,
                servingTemperature = 7.0,
                score = 92.0,
                price = 190000,
                style = "French Champagne",
                grade = "AOC(AOP)",
                importer = "레드와인앤제이와이",
                wineryId = 1,
                regionId = 3,
                aromas = listOf("리치","코코넛"),
                pairings = listOf("양갈비","치즈"),
                grapes = listOf(1),
            )
        fun 와인_동적_생성_요청_데이터(korName:String,engName:String,style:String,price:Int,score:Double,importer:String): WineCreateRequest =
            WineCreateRequest(
                korName = korName,
                engName = engName,
                type = WineType.SPARKLING,
                alcohol = 12.5,
                acidity = 4,
                body = 1,
                sweetness = 1,
                tannin = 1,
                servingTemperature = 7.0,
                score = score,
                price = price,
                style = style,
                grade = "AOC(AOP)",
                importer = importer,
                wineryId = 1,
                regionId = 3,
                aromas = listOf("리치","코코넛"),
                pairings = listOf("양갈비","치즈"),
                grapes = listOf(1),
            )

        fun 와인_수정_요청_데이터(): WineUpdateRequest =
            WineUpdateRequest(
                type = WineType.SPARKLING,
                korName = "앙또냉 귀용 볼네 프리미에 크뤼 끌로 데 셴느",
                engName = "Antonin Guyon Volnay Premier Cru Clos des Chenes",
                alcohol = 12.5,
                acidity = 4,
                body = 1,
                sweetness = 1,
                tannin = 1,
                score = 92.0,
                price = 190000,
                style = "French Champagne",
                grade = "AOC(AOP)",
                importer = "레드와인앤제이와이",
                servingTemperature = 7.0,
                wineryId = 1,
                regionId = 3,
                aromas = listOf("리치","코코넛","딸기"),
                pairings = listOf("양갈비","치즈","카나페"),
                grapes = listOf(1),
            )

    }
}
