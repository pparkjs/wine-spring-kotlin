package io.directional.wine.acceptance.region

import io.directional.wine.acceptance.common.AcceptanceTest
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_생성_요청_데이터
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_저장_API_호출
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_리스트_API_호출
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_삭제_API_호출
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_상세_조회_API_호출
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_부모_포함_요청_데이터_depth_2
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_부모_포함_요청_데이터_depth_3
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_요청_데이터_depth_1
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_수정_API_호출
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_수정_요청_데이터
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_저장_API_호출
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_생성_요청_데이터
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_저장_API_호출
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_생성_요청_데이터
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_저장_API_호출
import io.directional.wine.common.dto.ListPageResponse
import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.dto.region.response.*
import io.restassured.common.mapper.TypeRef
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.*

@DisplayName("지역 관련 인수테스트")
class RegionAcceptanceTest :AcceptanceTest() {

    @Nested
    inner class 지역_생성{
        @Test
        fun 최상위_지역을_성공적으로_생성한다(){
            // given
            val regionCreateRequest = 지역_생성_요청_데이터_depth_1()

            // when
            val response = 지역_저장_API_호출(regionCreateRequest)
            val result = response.`as`(object:TypeRef<ResultResponse<RegionCreateResponse>>() {})

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value())
            assertThat(result.result.id).isEqualTo(1L)
        }

        @Test
        fun 자식_지역을_성공적으로_생성한다(){
            // given
            val regionCreateRequest = 지역_생성_요청_데이터_depth_1()
            val regionCreateRequestWithParent = 지역_생성_부모_포함_요청_데이터_depth_2()

            // when
            지역_저장_API_호출(regionCreateRequest)
            val response = 지역_저장_API_호출(regionCreateRequestWithParent)
            val result = response.`as`(object:TypeRef<ResultResponse<RegionCreateResponse>>() {})

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value())
            assertThat(result.result.id).isEqualTo(2L)
            assertThat(result.result.parentId).isEqualTo(1L)
            assertThat(result.result.rootId).isEqualTo(1L)
            assertThat(result.result.depth).isEqualTo(2)
        }
    }

    @Nested
    inner class 지역_상세_조회{
        @Test
        fun 지역을_상세_조회한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            val 지역 = 지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_3())
                .`as`(object :TypeRef<ResultResponse<RegionCreateResponse>>() {}).result

            와이너리_저장_API_호출(와이너리_생성_요청_데이터())
            포도품종_저장_API_호출(포도품종_생성_요청_데이터())
            와인_저장_API_호출(와인_생성_요청_데이터())

            // when
            val result = 지역_상세_조회_API_호출(지역.id)
                .`as`(object : TypeRef<ResultResponse<RegionDetailResponse>>() {})
            // then
            val expect:RegionDetailResponse = RegionDetailResponse(
                id = 3,
                korName = "멘도치노 카운티",
                engName = "Mendocino County",
                parentRegions = listOf(
                    RegionParentResponse(korName = "캘리포니아", engName = "California", depth = 2),
                    RegionParentResponse(korName = "미국", engName = "U.S.A", depth = 1),
                ),
                grapes = listOf(
                    RegionGrapeResponse(korName = "바르베라", engName = "Barbera")
                ),
                wineries = listOf(
                    RegionWineryResponse(korName = "1881 나파", engName = "1881 Napa")
                ),
                wines = listOf(
                    RegionWineResponse(korName = "알프레드 그라티엔, 클래식 브뤼", engName = "Alfred Gratien, Classic Brut")
                )
            )

            assertThat(result).usingRecursiveComparison()
                .ignoringFieldsOfTypes(Long::class.java)
                .isEqualTo(ResultResponse(result = expect))
        }
    }

    @Nested
    inner class 지역_수정{

        @Test
        fun 존재하지_않는_지역은_수정할_수_없다(){
            // given
            val 존재하지_않는_지역_ID = 2L
            val regionUpdateRequest = 지역_수정_요청_데이터()

            // when
            val response = 지역_수정_API_호출(regionUpdateRequest, 존재하지_않는_지역_ID)

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value())
        }

        @Test
        fun 지역을_성공적으로_수정한다(){
            // given
            val 지역_ID = 지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
                .`as`(object:TypeRef<ResultResponse<RegionCreateResponse>>() {}).result.id
            val regionUpdateRequest = 지역_수정_요청_데이터()

            // when
            val response = 지역_수정_API_호출(regionUpdateRequest, 지역_ID)

            // then
            val 수정된_지역 = 지역_상세_조회_API_호출(지역_ID)
                .`as`(object : TypeRef<ResultResponse<RegionDetailResponse>>() {})

            assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value())
            assertThat(regionUpdateRequest.korName).isEqualTo(수정된_지역.result.korName)
            assertThat(regionUpdateRequest.engName).isEqualTo(수정된_지역.result.engName)
        }
    }

    @Nested
    inner class 지역_삭제{

        @Test
        fun 존재하지_않는_지역은_삭제할_수_없다(){
            // given
            val 존재하지_않는_지역_ID = 2L

            // when
            val response = 지역_삭제_API_호출(존재하지_않는_지역_ID)

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value())
        }

        @Test
        fun 지역을_성공적으로_삭제한다(){
            // given
            val 지역_ID = 지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
                .`as`(object:TypeRef<ResultResponse<RegionCreateResponse>>() {}).result.id

            // when
            val result = 지역_삭제_API_호출(지역_ID)

            // then
            assertThat(result.statusCode()).isEqualTo(NO_CONTENT.value())
            assertThat(지역_상세_조회_API_호출(지역_ID).statusCode()).isEqualTo(NOT_FOUND.value())
        }

    }
    @Nested
    inner class 지역_리스트_조회{

        @Test
        fun 지역_리스트를_성공적으로_조회한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())

            // when
            val response = 지역_리스트_API_호출(filter = 1, keyword = "캘리포")
            val result = response.`as`(object : TypeRef<ListPageResponse<RegionListResponse>>() {})

            // then
            val expect = ListPageResponse(
                result = listOf(
                    RegionListResponse(id = 2, korName = "캘리포니아", engName = "California"),
                    RegionListResponse(id = 3, korName = "캘리포니아", engName = "California"),
                    RegionListResponse(id = 4, korName = "캘리포니아", engName = "California"),
                    RegionListResponse(id = 5, korName = "캘리포니아", engName = "California"),
                ),
                totalCount = 4,
                hasNext = false
            )
            assertThat(result).usingRecursiveComparison()
                .isEqualTo(expect)
        }

    }
}
