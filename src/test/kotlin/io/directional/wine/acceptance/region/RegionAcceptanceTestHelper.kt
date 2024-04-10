package io.directional.wine.acceptance.region

import io.directional.wine.dto.region.request.RegionCreateRequest
import io.directional.wine.dto.region.request.RegionUpdateRequest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When

abstract class RegionAcceptanceTestHelper {

    companion object{
        fun 지역_저장_API_호출(request: RegionCreateRequest) =
            Given {
                log().all()
                contentType(ContentType.JSON)
                body(request)
            } When {
                post("/region")
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 지역_상세_조회_API_호출(regionId:Long) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                get("/region/{regionId}", regionId)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 지역_수정_API_호출(request: RegionUpdateRequest, regionId:Long) =
            Given {
                log().all()
                contentType(ContentType.JSON)
                body(request)
            } When {
                patch("/region/{regionId}", regionId)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 지역_삭제_API_호출(regionId:Long) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                delete("/region/{regionId}", regionId)
            } Then {
                log().all()
            } Extract {
                this
            }

        fun 지역_리스트_API_호출(filter:Long, keyword:String) =
            Given {
                log().all()
                contentType(ContentType.JSON)
            } When {
                get("/region?page=0&size=10&filter={filter}&keyword={keyword}", filter, keyword)
            } Then {
                log().all()
            } Extract {
                this
            }
        fun 지역_생성_요청_데이터_depth_1():RegionCreateRequest =
            RegionCreateRequest(
                korName = "미국",
                engName = "U.S.A",
            )

        fun 지역_생성_요청_데이터2_depth_1():RegionCreateRequest =
            RegionCreateRequest(
                korName = "칠레",
                engName = "Chile",
            )

        fun 지역_생성_부모_포함_요청_데이터_depth_2():RegionCreateRequest =
            RegionCreateRequest(
                korName = "캘리포니아",
                engName = "California",
                parentId = 1
            )

        fun 지역_생성_부모_포함_요청_데이터_depth_3():RegionCreateRequest =
            RegionCreateRequest(
                korName = "멘도치노 카운티",
                engName = "Mendocino County",
                parentId = 2
            )

        fun 지역_수정_요청_데이터():RegionUpdateRequest =
            RegionUpdateRequest(
                korName = "캐나다",
                engName = "Canada",
            )

    }
}
