package io.directional.wine.acceptance.winery

import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.dto.region.request.RegionCreateRequest
import io.directional.wine.dto.region.request.RegionUpdateRequest
import io.directional.wine.dto.region.response.RegionDetailResponse
import io.directional.wine.dto.winery.request.WineryCreateRequest
import io.directional.wine.dto.winery.request.WineryUpdateRequest
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When

abstract class WineryAcceptanceTestHelper {

    companion object{
        fun 와이너리_저장_API_호출(request: WineryCreateRequest) =
            Given {
                log().all()
                contentType(ContentType.JSON)
                body(request)
            } When {
                post("/winery")
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 와이너리_상세_조회_API_호출(wineryId:Long) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                get("/winery/{wineryId}", wineryId)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 와이너리_수정_API_호출(request: WineryUpdateRequest, wineryId:Long) =
            Given {
                log().all()
                contentType(ContentType.JSON)
                body(request)
            } When {
                patch("/winery/{wineryId}", wineryId)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 와이너리_삭제_API_호출(wineryId:Long) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                delete("/winery/{wineryId}", wineryId)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 와이너리_리스트_API_호출(filter:Long, keyword:String) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                get("/winery?page=0&size=3&filter={filter}&keyword={keyword}", filter, keyword)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 와이너리_생성_요청_데이터():WineryCreateRequest =
            WineryCreateRequest(
                korName = "1881 나파",
                engName = "1881 Napa",
                regionId = 1,
            )

        fun 와이너리_동적_생성_요청_데이터(korName:String, engName:String, regionId:Long):WineryCreateRequest =
            WineryCreateRequest(
                korName = korName,
                engName = engName,
                regionId = regionId,
            )

        fun 와이너리_수정_요청_데이터(): WineryUpdateRequest =
            WineryUpdateRequest(
                korName = "앤드류 윌 와이너리",
                engName = "Andrew Will Winery",
            )

    }
}
