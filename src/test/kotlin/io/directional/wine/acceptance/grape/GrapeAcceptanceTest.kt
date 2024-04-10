package io.directional.wine.acceptance.grape

import io.directional.wine.acceptance.common.AcceptanceTest
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_동적_생성_요청_데이터
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_리스트_API_호출
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_삭제_API_호출
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_상세_조회_API_호출
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_생성_요청_데이터
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_수정_API_호출
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_수정_요청_데이터
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_저장_API_호출
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_상세_조회_API_호출
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_부모_포함_요청_데이터_depth_2
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_부모_포함_요청_데이터_depth_3
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_요청_데이터2_depth_1
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_요청_데이터_depth_1
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_수정_API_호출
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_수정_요청_데이터
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_저장_API_호출
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_생성_요청_데이터
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_저장_API_호출
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_생성_요청_데이터
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_저장_API_호출
import io.directional.wine.common.dto.ListPageResponse
import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.common.util.findByIdOrThrow
import io.directional.wine.dto.grape.request.GrapeUpdateRequest
import io.directional.wine.dto.grape.request.ShareRequest
import io.directional.wine.dto.grape.response.*
import io.directional.wine.dto.region.response.*
import io.directional.wine.dto.winery.response.WineryListResponse
import io.restassured.common.mapper.TypeRef
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.http.HttpStatus.*

@DisplayName("포도품종 관련 인수테스트")
class GrapeAcceptanceTest :AcceptanceTest() {

    @Nested
    inner class 포도품종_생성{
        @Test
        fun 포도품종을_성공적으로_생성한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            val grapeCreateRequest = 포도품종_생성_요청_데이터()

            // when
            val response = 포도품종_저장_API_호출(grapeCreateRequest)
            val result = response.`as`(object:TypeRef<ResultResponse<GrapeCreateResponse>>() {})

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value())
            assertThat(result.result.id).isEqualTo(1L)
        }
    }

    @Nested
    inner class 포도품종_상세_조회{
        @Test
        fun 포도품종을_상세_조회한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_3())
            val 포도품종 = 포도품종_저장_API_호출(포도품종_생성_요청_데이터())
                .`as`(object :TypeRef<ResultResponse<GrapeCreateResponse>>() {}).result
            와이너리_저장_API_호출(와이너리_생성_요청_데이터())
            와인_저장_API_호출(와인_생성_요청_데이터())

            // when
            val result = 포도품종_상세_조회_API_호출(포도품종.id)
                .`as`(object : TypeRef<ResultResponse<GrapeDetailResponse>>() {})

            // then
            val expect:GrapeDetailResponse = GrapeDetailResponse(
                id = 1,
                grapeKorName = "바르베라",
                grapeEngName = "Barbera",
                acidity = 4,
                body = 3,
                sweetness = 1,
                tannin = 3,
                regions = listOf(
                    GrapeRegionResponse(korName = "미국", engName = "U.S.A")
                ),
                wines = listOf(
                    GrapeWineResponse(korName = "알프레드 그라티엔, 클래식 브뤼", engName = "Alfred Gratien, Classic Brut")
                )
            )
            assertThat(result).usingRecursiveComparison()
                .ignoringFieldsOfTypes(Long::class.java)
                .isEqualTo(ResultResponse(result = expect))
        }
    }

    @Nested
    inner class 포도품종_수정{

        @Test
        fun 존재하지_않는_포도품종은_수정할_수_없다(){
            // given
            val 존재하지_않는_포도품종_ID = 2L
            val grapeUpdateRequest = 포도품종_수정_요청_데이터()

            // when
            val response = 포도품종_수정_API_호출(grapeUpdateRequest, 존재하지_않는_포도품종_ID)

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value())
        }

        @Test
        fun 포도품종을_성공적으로_수정한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            지역_저장_API_호출(지역_생성_요청_데이터2_depth_1())
            val 포도품종 = 포도품종_저장_API_호출(포도품종_생성_요청_데이터())
                .`as`(object :TypeRef<ResultResponse<GrapeCreateResponse>>() {}).result
            val grapeUpdateRequest = 포도품종_수정_요청_데이터()

            // when
            val response = 포도품종_수정_API_호출(grapeUpdateRequest, 포도품종.id)

            // then
            val 수정된_포도품종 = 포도품종_상세_조회_API_호출(포도품종.id)
                .`as`(object : TypeRef<ResultResponse<GrapeDetailResponse>>() {})

            assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value())
            assertThat(grapeUpdateRequest.korName).isEqualTo(수정된_포도품종.result.grapeKorName)
            assertThat(grapeUpdateRequest.engName).isEqualTo(수정된_포도품종.result.grapeEngName)
            assertThat(grapeUpdateRequest.acidity).isEqualTo(수정된_포도품종.result.acidity)
            assertThat(grapeUpdateRequest.body).isEqualTo(수정된_포도품종.result.body)
            assertThat(grapeUpdateRequest.sweetness).isEqualTo(수정된_포도품종.result.sweetness)
            assertThat(grapeUpdateRequest.tannin).isEqualTo(수정된_포도품종.result.tannin)
        }
    }

    @Nested
    inner class 포도품종_삭제{

        @Test
        fun 존재하지_않는_포도품종은_삭제할_수_없다(){
            // given
            val 존재하지_않는_포도품종_ID = 2L

            // when
            val response = 포도품종_삭제_API_호출(존재하지_않는_포도품종_ID)

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value())
        }
        @Test
        fun 포도품종을_성공적으로_삭제한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            val 포도품종 = 포도품종_저장_API_호출(포도품종_생성_요청_데이터())
                .`as`(object :TypeRef<ResultResponse<GrapeCreateResponse>>() {}).result

            // when
            val result = 포도품종_삭제_API_호출(포도품종.id)

            // then
            assertThat(result.statusCode()).isEqualTo(NO_CONTENT.value())
            assertThat(포도품종_상세_조회_API_호출(포도품종.id).statusCode()).isEqualTo(NOT_FOUND.value())
        }
    }

    @Nested
    inner class 포도품종_리스트_조회 {

        @Test
        fun 포도품종을_리스트를_성공적으로_조회한다() {
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            포도품종_저장_API_호출(포도품종_동적_생성_요청_데이터(korName = "카베르네 프랑", engName = "Cabernet Franc"))
            포도품종_저장_API_호출(포도품종_동적_생성_요청_데이터(korName = "카베르네 소비뇽", engName = "Cabernet Sauvignon"))
            포도품종_저장_API_호출(포도품종_동적_생성_요청_데이터(korName = "오쎄후와", engName = "Auxerrois"))
            포도품종_저장_API_호출(포도품종_동적_생성_요청_데이터(korName = "카나이올로", engName = "Canaiolo"))
            포도품종_저장_API_호출(포도품종_동적_생성_요청_데이터(korName = "까리냥", engName = "Carignan/Carignane"))
            포도품종_저장_API_호출(포도품종_동적_생성_요청_데이터(korName = "코르비나 베로네제", engName = "Corvina Veronese"))
            // when
            val response = 포도품종_리스트_API_호출(filter = 1, keyword = "ca", order = "grape", sort = "desc")
            val result = response.`as`(object : TypeRef<ListPageResponse<GrapeListResponse>>() {})

            // then
            val expect = ListPageResponse(
                result = listOf(
                    GrapeListResponse(
                        id = 5,
                        korName = "까리냥",
                        engName = "Carignan/Carignane",
                        regions = listOf(GrapeRegionResponse(korName = "미국", engName = "U.S.A"))
                    ),
                    GrapeListResponse(
                        id = 4,
                        korName = "카나이올로",
                        engName = "Canaiolo",
                        regions = listOf(GrapeRegionResponse(korName = "미국", engName = "U.S.A"))
                    ),
                    GrapeListResponse(
                        id = 2,
                        korName = "카베르네 소비뇽",
                        engName = "Cabernet Sauvignon",
                        regions = listOf(GrapeRegionResponse(korName = "미국", engName = "U.S.A"))
                    ),
                    GrapeListResponse(
                        id = 1,
                        korName = "카베르네 프랑",
                        engName = "Cabernet Franc",
                        regions = listOf(GrapeRegionResponse(korName = "미국", engName = "U.S.A"))
                    ),
                ),
                totalCount = 4,
                hasNext = false
            )
            assertThat(result).usingRecursiveComparison()
                .isEqualTo(expect)
        }
    }
}
