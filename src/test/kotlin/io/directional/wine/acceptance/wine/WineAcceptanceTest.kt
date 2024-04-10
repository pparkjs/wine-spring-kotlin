package io.directional.wine.acceptance.wine

import io.directional.wine.acceptance.common.AcceptanceTest
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_생성_요청_데이터
import io.directional.wine.acceptance.grape.GrapeAcceptanceTestHelper.Companion.포도품종_저장_API_호출
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_부모_포함_요청_데이터_depth_2
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_부모_포함_요청_데이터_depth_3
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_생성_요청_데이터_depth_1
import io.directional.wine.acceptance.region.RegionAcceptanceTestHelper.Companion.지역_저장_API_호출
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.수입사_리스트_API_호출
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.수입사_상세_조회_API_호출
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_동적_생성_요청_데이터
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_리스트_API_호출
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_삭제_API_호출
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_상세_조회_API_호출
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_생성_요청_데이터
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_수정_API_호출
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_수정_요청_데이터
import io.directional.wine.acceptance.wine.WineAcceptanceTestHelper.Companion.와인_저장_API_호출
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_생성_요청_데이터
import io.directional.wine.acceptance.winery.WineryAcceptanceTestHelper.Companion.와이너리_저장_API_호출
import io.directional.wine.common.dto.ListPageResponse
import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.domain.wine.WineType
import io.directional.wine.dto.grape.response.GrapeDetailResponse
import io.directional.wine.dto.grape.response.GrapeListResponse
import io.directional.wine.dto.grape.response.GrapeRegionResponse
import io.directional.wine.dto.grape.response.GrapeWineResponse
import io.directional.wine.dto.region.response.RegionCreateResponse
import io.directional.wine.dto.region.response.RegionParentResponse
import io.directional.wine.dto.wine.request.WineFilter
import io.directional.wine.dto.wine.response.*
import io.restassured.common.mapper.TypeRef
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.*

@DisplayName("와인 관련 인수테스트")
class WineAcceptanceTest :AcceptanceTest() {

    @Nested
    inner class 와인_생성{
        @Test
        fun 와인을_성공적으로_생성한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_3())
            와이너리_저장_API_호출(와이너리_생성_요청_데이터())
            포도품종_저장_API_호출(포도품종_생성_요청_데이터())
            포도품종_저장_API_호출(포도품종_생성_요청_데이터())

            // when
            val response = 와인_저장_API_호출(와인_생성_요청_데이터())
            val result = response.`as`(object:TypeRef<ResultResponse<WineCreateResponse>>() {})

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value())
            assertThat(result.result.id).isEqualTo(1L)
        }
    }

    @Nested
    inner class 와인_상세_조회{
        @Test
        fun 와인을_상세_조회한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_3())
            와이너리_저장_API_호출(와이너리_생성_요청_데이터())
            포도품종_저장_API_호출(포도품종_생성_요청_데이터())
            val 와인 = 와인_저장_API_호출(와인_생성_요청_데이터())
                .`as`(object:TypeRef<ResultResponse<WineCreateResponse>>() {}).result

            // when
            val result = 와인_상세_조회_API_호출(와인.id)
                .`as`(object : TypeRef<ResultResponse<WineDetailResponse>>() {})

            // then
            val expect:WineDetailResponse = WineDetailResponse(
                id = 1,
                type = WineType.SPARKLING,
                korName = "알프레드 그라티엔, 클래식 브뤼",
                engName = "Alfred Gratien, Classic Brut",
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
                wineryKorName = "1881 나파",
                wineryEngName = "1881 Napa",
                wineryRegionKorName = "미국",
                wineryRegionEngName = "U.S.A",
                regionKorName = "멘도치노 카운티",
                regionEngName = "Mendocino County",
                parentRegions = listOf(
                    RegionParentResponse(korName = "캘리포니아", engName = "California", depth = 2),
                    RegionParentResponse(korName = "미국", engName = "U.S.A", depth = 1),
                ),
                grapes = listOf(
                    WineGrapeResponse(korName = "바르베라", engName = "Barbera"),
                ),
                aromas = listOf("리치","코코넛"),
                pairings = listOf("양갈비","치즈"),
            )
            assertThat(result).usingRecursiveComparison()
                .ignoringFieldsOfTypes(Long::class.java)
                .isEqualTo(ResultResponse(result = expect))
        }
    }

    @Nested
    inner class 와인_수정{

        @Test
        fun 존재하지_않는_와인은_수정할_수_없다(){
            // given
            val 존재하지_않는_와인_ID = 2L
            val wineUpdateRequest = 와인_수정_요청_데이터()

            // when
            val response = 와인_수정_API_호출(wineUpdateRequest, 존재하지_않는_와인_ID)

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value())
        }

        @Test
        fun 와인을_성공적으로_수정한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_3())
            와이너리_저장_API_호출(와이너리_생성_요청_데이터())
            포도품종_저장_API_호출(포도품종_생성_요청_데이터())
            val 와인 = 와인_저장_API_호출(와인_생성_요청_데이터())
                .`as`(object:TypeRef<ResultResponse<WineCreateResponse>>() {}).result
            val wineUpdateRequest = 와인_수정_요청_데이터()

            // when
            val response = 와인_수정_API_호출(wineUpdateRequest, 와인.id)

            // then
            val 수정된_와인 = 와인_상세_조회_API_호출(와인.id)
                .`as`(object : TypeRef<ResultResponse<WineDetailResponse>>() {})
            assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value())
            assertThat(wineUpdateRequest.korName).isEqualTo(수정된_와인.result.korName)
            assertThat(wineUpdateRequest.engName).isEqualTo(수정된_와인.result.engName)
        }
    }

    @Nested
    inner class 와인_삭제{

        @Test
        fun 존재하지_않는_와인은_삭제할_수_없다(){
            // given
            val 존재하지_않는_와인_ID = 2L

            // when
            val response = 와인_삭제_API_호출(존재하지_않는_와인_ID)

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value())
        }
        @Test
        fun 와인을_성공적으로_삭제한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_3())
            와이너리_저장_API_호출(와이너리_생성_요청_데이터())
            포도품종_저장_API_호출(포도품종_생성_요청_데이터())
            val 와인 = 와인_저장_API_호출(와인_생성_요청_데이터())
                .`as`(object:TypeRef<ResultResponse<WineCreateResponse>>() {}).result

            // when
            val result = 와인_삭제_API_호출(와인.id)

            // then
            assertThat(result.statusCode()).isEqualTo(NO_CONTENT.value())
            assertThat(와인_상세_조회_API_호출(와인.id).statusCode()).isEqualTo(NOT_FOUND.value())
        }
    }

    @Nested
    inner class 와인_리스트_조회{

        @Test
        fun 와인_리스트를_성공적으로_조회한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_3())
            와이너리_저장_API_호출(와이너리_생성_요청_데이터())
            포도품종_저장_API_호출(포도품종_생성_요청_데이터())
            와인_저장_API_호출(와인_동적_생성_요청_데이터(
                korName = "앤드류 윌, 샴푸 빈야드",
                engName = "Andrew Will, Champoux Vineyard",
                style = "Washington State Red Blend",
                price = 200000,
                score = 90.4,
                importer = "레드와인앤제이와이"
            ))
            와인_저장_API_호출(와인_동적_생성_요청_데이터(
                korName = "앤드류 윌, 소렐라 레드 와인",
                engName = "Andrew will, Sorella Red Wine",
                style = "Washington State Red Blend",
                price = 160000,
                score = 92.1,
                importer = "레드와인앤제이와이"
            ))
            와인_저장_API_호출(와인_동적_생성_요청_데이터(
                korName = "앙또냉 귀용 볼네 프리미에 크뤼 끌로 데 셴느",
                engName = "Antonin Guyon Volnay Premier Cru Clos des Chenes",
                style = "Washington State Red Blend",
                price = 210000,
                score = 89.0,
                importer = "레드와인앤제이와이"
            ))
            와인_저장_API_호출(와인_동적_생성_요청_데이터(
                korName = "앤드류 윌, 투 블론즈 빈야드 레드 와인",
                engName = "Andrew Will, Two Blondes Vineyard Red Wine",
                style = "Italian Nebbiolo",
                price = 250000,
                score = 95.5,
                importer = "레드와인앤제이와이"
            ))
            val filter = WineFilter(
                wineType = null,
                minAlcohol = null, maxAlcohol = null,
                minPrice = 200000, maxPrice = 250000,
                wineStyle = "Washington State Red Blend",
                wineGrade = null,
                region = 3,
            )

            // when
            val response =
                와인_리스트_API_호출(filter = filter, keyword = "an", order = "score", sort = "desc")
            val result = response.`as`(object : TypeRef<ListPageResponse<WineListResponse>>() {})

            // then
            val expect = ListPageResponse(
                result = listOf(
                    WineListResponse(
                        id = 1,
                        type = WineType.SPARKLING,
                        korName = "앤드류 윌, 샴푸 빈야드",
                        engName = "Andrew Will, Champoux Vineyard",
                        topLevelKorRegionName = "미국",
                        topLevelEngRegionName = "U.S.A",
                    ),
                    WineListResponse(
                        id = 3,
                        type = WineType.SPARKLING,
                        korName = "앙또냉 귀용 볼네 프리미에 크뤼 끌로 데 셴느",
                        engName = "Antonin Guyon Volnay Premier Cru Clos des Chenes",
                        topLevelKorRegionName = "미국",
                        topLevelEngRegionName = "U.S.A",
                    ),
                ),
                totalCount = 2,
                hasNext = false
            )
            assertThat(result).usingRecursiveComparison()
                .isEqualTo(expect)

        }
    }
    @Nested
    inner class 수입사_조회{

        @Test
        fun 수입사를_상세_조회한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_3())
            와이너리_저장_API_호출(와이너리_생성_요청_데이터())
            포도품종_저장_API_호출(포도품종_생성_요청_데이터())
            와인_저장_API_호출(와인_생성_요청_데이터())

            // when
            val result = 수입사_상세_조회_API_호출("레드와인앤제이와이")
                .`as`(object : TypeRef<ResultResponse<ImporterDetailResponse>>() {})

            // then
            val expect:ImporterDetailResponse = ImporterDetailResponse(
                importerName = "레드와인앤제이와이",
                wineNames = listOf(
                    WineImporterResponse(korName = "알프레드 그라티엔, 클래식 브뤼", engName = "Alfred Gratien, Classic Brut"),
                ),
            )
            assertThat(result).usingRecursiveComparison()
                .ignoringFieldsOfTypes(Long::class.java)
                .isEqualTo(ResultResponse(result = expect))
        }

        @Test
        fun 수입사_리스트를_성공적으로_조회한다(){
            // given
            지역_저장_API_호출(지역_생성_요청_데이터_depth_1())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_2())
            지역_저장_API_호출(지역_생성_부모_포함_요청_데이터_depth_3())
            와이너리_저장_API_호출(와이너리_생성_요청_데이터())
            포도품종_저장_API_호출(포도품종_생성_요청_데이터())

            와인_저장_API_호출(와인_동적_생성_요청_데이터(
                korName = "앙또냉 귀용 볼네 프리미에 크뤼 끌로 데 셴느",
                engName = "Antonin Guyon Volnay Premier Cru Clos des Chenes",
                style = "Washington State Red Blend",
                price = 210000,
                score = 89.0,
                importer = "나라셀라"
            ))
            와인_저장_API_호출(와인_동적_생성_요청_데이터(
                korName = "앤드류 윌, 샴푸 빈야드",
                engName = "Andrew Will, Champoux Vineyard",
                style = "Washington State Red Blend",
                price = 200000,
                score = 90.4,
                importer = "레드와인앤제이와이"
            ))
            와인_저장_API_호출(와인_동적_생성_요청_데이터(
                korName = "앤드류 윌, 소렐라 레드 와인",
                engName = "Andrew will, Sorella Red Wine",
                style = "Washington State Red Blend",
                price = 160000,
                score = 92.1,
                importer = "레드와인앤제이와이"
            ))
            와인_저장_API_호출(와인_동적_생성_요청_데이터(
                korName = "앙또냉 귀용 볼네 프리미에 크뤼 끌로 데 셴느",
                engName = "Antonin Guyon Volnay Premier Cru Clos des Chenes",
                style = "Washington State Red Blend",
                price = 210000,
                score = 89.0,
                importer = "레노테카코리아"
            ))
            와인_저장_API_호출(와인_동적_생성_요청_데이터(
                korName = "앤드류 윌, 투 블론즈 빈야드 레드 와인",
                engName = "Andrew Will, Two Blondes Vineyard Red Wine",
                style = "Italian Nebbiolo",
                price = 250000,
                score = 95.5,
                importer = "레뱅"
            ))

            // when
            val response = 수입사_리스트_API_호출(keyword = "레")
            val result = response.`as`(object : TypeRef<ListPageResponse<String>>() {})

            // then
            val expect = ListPageResponse(
                result = listOf("레노테카코리아", "레드와인앤제이와이", "레뱅"),
                totalCount = 3,
                hasNext = false
            )
            assertThat(result).usingRecursiveComparison()
                .isEqualTo(expect)
        }
    }

}
