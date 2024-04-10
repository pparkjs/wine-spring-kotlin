package io.directional.wine.acceptance.grape

import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.dto.grape.request.GrapeCreateRequest
import io.directional.wine.dto.grape.request.GrapeUpdateRequest
import io.directional.wine.dto.grape.request.ShareRequest
import io.directional.wine.dto.region.request.RegionCreateRequest
import io.directional.wine.dto.region.request.RegionUpdateRequest
import io.directional.wine.dto.region.response.RegionDetailResponse
import io.directional.wine.dto.winery.request.WineryCreateRequest
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When

abstract class GrapeAcceptanceTestHelper {

    companion object{
        fun 포도품종_저장_API_호출(request: GrapeCreateRequest) =
            Given {
                log().all()
                contentType(ContentType.JSON)
                body(request)
            } When {
                post("/grape")
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 포도품종_상세_조회_API_호출(grapeId:Long) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                get("/grape/{grapeId}", grapeId)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 포도품종_수정_API_호출(request: GrapeUpdateRequest, grapeId:Long) =
            Given {
                log().all()
                contentType(ContentType.JSON)
                body(request)
            } When {
                patch("/grape/{grapeId}", grapeId)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 포도품종_삭제_API_호출(grapeId:Long) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                delete("/grape/{grapeId}", grapeId)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 포도품종_리스트_API_호출(filter:Long, keyword:String, order:String, sort:String) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                get("/grape?page=0&size=5&filter={filter}&keyword={keyword}&order={order}&sort={sort}", filter, keyword, order, sort)
            } Then {
                log().all()
            } Extract {
                this
            }
        fun 포도품종_생성_요청_데이터(): GrapeCreateRequest =
            GrapeCreateRequest(
                korName = "바르베라",
                engName = "Barbera",
                acidity = 4,
                body = 3,
                sweetness = 1,
                tannin = 3,
                shares = listOf(
                    ShareRequest(share = 2.5, regionId = 1),
                ),
            )

        fun 포도품종_동적_생성_요청_데이터(korName:String,engName:String): GrapeCreateRequest =
            GrapeCreateRequest(
                korName = korName,
                engName = engName,
                acidity = 4,
                body = 3,
                sweetness = 1,
                tannin = 3,
                shares = listOf(
                    ShareRequest(share = 2.5, regionId = 1),
                ),
            )


        fun 포도품종_수정_요청_데이터(): GrapeUpdateRequest =
            GrapeUpdateRequest(
                korName = "아르반",
                engName = "Arbanne",
                acidity = 5,
                body = 2,
                sweetness = 1,
                tannin = 1,
                shares = listOf(
                    ShareRequest(share = 3.0, regionId = 1),
                    ShareRequest(share = 3.2, regionId = 2),
                ),
            )

    }
}
