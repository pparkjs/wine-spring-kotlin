package io.directional.wine.acceptance.winery

import io.directional.wine.acceptance.common.AcceptanceTest
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_생성_요청_데이터
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_저장_API_호출
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_부모_포함_요청_데이터_depth_2
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_부모_포함_요청_데이터_depth_3
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_요청_데이터_depth_1
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_저장_API_호출
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_생성_요청_데이터
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_저장_API_호출
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_동적_생성_요청_데이터
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_리스트_API_호출
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_삭제_API_호출
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_상세_조회_API_호출
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_생성_요청_데이터
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_수정_API_호출
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_수정_요청_데이터
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_저장_API_호출
import io.directional.wine.common.dto.ListPageResponse
import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.dto.region.response.*
import io.directional.wine.dto.winery.response.WineryCreateResponse
import io.directional.wine.dto.winery.response.WineryDetailResponse
import io.directional.wine.dto.winery.response.WineryListResponse
import io.directional.wine.dto.winery.response.WineryWineResponse
import io.restassured.common.mapper.TypeRef
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.*

@DisplayName("와이너리 관련 인수테스트")
class WineryAcceptanceTest :AcceptanceTest() {

    @Nested
    inner class 와이너리_생성{
        @Test
        fun 와이너리를_성공적으로_생성한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())

            // when
            val response = 와이너리_저장_API_호출(와이너리_생성_요청_데이터())
            val result = response.`as`(object:TypeRef<ResultResponse<WineryCreateResponse>>() {})

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value())
            assertThat(result.result.id).isEqualTo(1L)
        }
    }

    @Nested
    inner class 와이너리_상세_조회{
        @Test
        fun 와이너리를_상세_조회한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_3())
            val 와이너리 = 와이너리_저장_API_호출(와이너리_생성_요청_데이터())
                .`as`(object:TypeRef<ResultResponse<WineryCreateResponse>>() {})
            포도품종_저장_API_호출(포도품종_생성_요청_데이터())
            와인_저장_API_호출(와인_생성_요청_데이터())

            // when
            val result = 와이너리_상세_조회_API_호출(와이너리.result.id)
                .`as`(object : TypeRef<ResultResponse<WineryDetailResponse>>() {})

            // then
            val expect:WineryDetailResponse = WineryDetailResponse(
                id = 1,
                korName = "1881 나파",
                engName = "1881 Napa",
                regionKorName = "미국",
                regionEngName = "U.S.A",
                wines = listOf(
                    WineryWineResponse(korName = "알프레드 그라티엔, 클래식 브뤼", engName = "Alfred Gratien, Classic Brut")
                )
            )
            assertThat(result).usingRecursiveComparison()
                .ignoringFieldsOfTypes(Long::class.java)
                .isEqualTo(ResultResponse(result = expect))
        }
    }

    @Nested
    inner class 와이너리_수정{

        @Test
        fun 존재하지_않는_와이너리는_수정할_수_없다(){
            // given
            val 존재하지_않는_와이너리_ID = 2L
            val wineryUpdateRequest = 와이너리_수정_요청_데이터()

            // when
            val response = 와이너리_수정_API_호출(wineryUpdateRequest, 존재하지_않는_와이너리_ID)

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value())
        }

        @Test
        fun 와이너리를_성공적으로_수정한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            val 와이너리 = 와이너리_저장_API_호출(와이너리_생성_요청_데이터())
                .`as`(object:TypeRef<ResultResponse<WineryCreateResponse>>() {})
            val wineryUpdateRequest = 와이너리_수정_요청_데이터()

            // when
            val response = 와이너리_수정_API_호출(wineryUpdateRequest, 와이너리.result.id)

            // then
            val 수정된_와이너리 = 와이너리_상세_조회_API_호출(와이너리.result.id)
                .`as`(object : TypeRef<ResultResponse<WineryDetailResponse>>() {})

            assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value())
            assertThat(wineryUpdateRequest.korName).isEqualTo(수정된_와이너리.result.korName)
            assertThat(wineryUpdateRequest.engName).isEqualTo(수정된_와이너리.result.engName)
        }
    }

    @Nested
    inner class 와이너리_삭제{

        @Test
        fun 존재하지_않는_와이너리는_삭제할_수_없다(){
            // given
            val 존재하지_않는_와이너리_ID = 2L

            // when
            val response = 와이너리_삭제_API_호출(존재하지_않는_와이너리_ID)

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value())
        }

        @Test
        fun 와이너리를_성공적으로_삭제한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            val 와이너리 = 와이너리_저장_API_호출(와이너리_생성_요청_데이터())
                .`as`(object:TypeRef<ResultResponse<WineryCreateResponse>>() {})

            // when
            val result = 와이너리_삭제_API_호출(와이너리.result.id)

            // then
            assertThat(result.statusCode()).isEqualTo(NO_CONTENT.value())
            assertThat(와이너리_상세_조회_API_호출(와이너리.result.id).statusCode()).isEqualTo(NOT_FOUND.value())
        }
    }

    @Nested
    inner class 와이너리_리스트_조회{

        @Test
        fun 와이너리_리스트를_성공적으로_조회한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            와이너리_저장_API_호출(와이너리_동적_생성_요청_데이터(korName = "알프레드 그라티엔", engName = "Alfred Gratien", regionId = 1))
            와이너리_저장_API_호출(와이너리_동적_생성_요청_데이터(korName = "알타 비스타", engName = "Alta Vista", regionId = 1))
            와이너리_저장_API_호출(와이너리_동적_생성_요청_데이터(korName = "알바로 팔라시오스", engName = "Alvaro Palacios", regionId = 1))
            와이너리_저장_API_호출(와이너리_동적_생성_요청_데이터(korName = "앙드르 끌루에", engName = "Andre Clouet", regionId = 1))
            와이너리_저장_API_호출(와이너리_동적_생성_요청_데이터(korName = "앙또넹 기용", engName = "Antonin Guyon [Domaine Antonin Guyon]", regionId = 1))
            와이너리_저장_API_호출(와이너리_동적_생성_요청_데이터(korName = "알레그리니", engName = "Allegrini", regionId = 1))

            // when
            val response = 와이너리_리스트_API_호출(filter = 1, keyword = "알")
            val result = response.`as`(object : TypeRef<ListPageResponse<WineryListResponse>>() {})

            // then
            val expect = ListPageResponse(
                result = listOf(
                    WineryListResponse(id = 1, korName = "알프레드 그라티엔", engName = "Alfred Gratien", regionKorName = "미국", regionEngName = "U.S.A"),
                    WineryListResponse(id = 6, korName = "알레그리니", engName = "Allegrini", regionKorName = "미국", regionEngName = "U.S.A"),
                    WineryListResponse(id = 2, korName = "알타 비스타", engName = "Alta Vista", regionKorName = "미국", regionEngName = "U.S.A"),
                ),
                totalCount = 4,
                hasNext = true
            )
            assertThat(result).usingRecursiveComparison()
                .isEqualTo(expect)
        }
    }


}
