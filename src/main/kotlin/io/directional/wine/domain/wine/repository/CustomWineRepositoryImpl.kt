package io.directional.wine.domain.wine.repository

import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import io.directional.wine.common.util.checkEndPage
import io.directional.wine.domain.region.QRegion
import io.directional.wine.domain.wine.QWine.wine
import io.directional.wine.domain.wine.WineType
import io.directional.wine.dto.wine.request.WineFilter
import io.directional.wine.dto.wine.response.WineListResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class CustomWineRepositoryImpl(
    private val query: JPAQueryFactory
): CustomWineRepository {
    override fun getWinesCount(keyword: String?, filter: WineFilter): Long? {
        return query.select(wine.count())
            .from(wine)
            .where(
                wineNameSearch(keyword),
                alcoholRangeBetween(filter.minAlcohol, filter.maxAlcohol),
                priceRangeBetween(filter.minPrice, filter.maxPrice),
                wineTypeEq(filter.wineType),
                wineStyleEq(filter.wineStyle),
                wineGradeEq(filter.wineGrade),
                regionEq(filter.region),
            )
            .fetchOne()
    }

    override fun getWines(pageable: Pageable, keyword: String?, filter: WineFilter, order: String): Slice<WineListResponse> {
        val subRegion1 = QRegion("subRegion")
        val subRegion2 = QRegion("subRegion")
        val orderSpecifiers = createOrderSpecifier(pageable)

        val result = query.select(
            Projections.constructor(
                WineListResponse::class.java,
                wine.id,
                wine.type,
                wine.name.korName,
                wine.name.engName,
                // 최상위 지역 이름 가져오기 위한 subQuery
                JPAExpressions.select(subRegion1.name.korName)
                    .from(subRegion1)
                    .where(
                        rootRegionSub(subRegion1, subRegion2)
                    ),
                JPAExpressions.select(subRegion1.name.engName)
                    .from(subRegion1)
                    .where(
                        rootRegionSub(subRegion1, subRegion2)
                    ),
            )
        )
        .from(wine)
        .where(
            wineNameSearch(keyword),
            alcoholRangeBetween(filter.minAlcohol, filter.maxAlcohol),
            priceRangeBetween(filter.minPrice, filter.maxPrice),
            wineTypeEq(filter.wineType),
            wineStyleEq(filter.wineStyle),
            wineGradeEq(filter.wineGrade),
            regionEq(filter.region),
        )
        .limit(pageable.pageSize.toLong() + 1)
        .offset(pageable.offset)
        .orderBy(orderSpecifiers)
        .fetch()

        return checkEndPage(pageable,result)
    }

    private fun rootRegionSub(subRegion1: QRegion, subRegion2: QRegion): BooleanExpression? =
        subRegion1.id.eq(
            JPAExpressions.select(subRegion2.rootId)
                .from(subRegion2)
                .where(subRegion2.id.eq(wine.region.id))
        )

    private fun createOrderSpecifier(pageable: Pageable): OrderSpecifier<*>? {
        if(!pageable.sort.isEmpty){
            pageable.sort.forEach { order ->
                val direction = if (order.isAscending) Order.ASC else Order.DESC
                return when (order.property){
                    "wine" -> OrderSpecifier(direction, wine.name.engName)
                    "alcohol" -> OrderSpecifier(direction, wine.alcohol)
                    "acidity" -> OrderSpecifier(direction, wine.acidity)
                    "body" -> OrderSpecifier(direction, wine.body)
                    "sweetness" -> OrderSpecifier(direction, wine.sweetness)
                    "tannin" -> OrderSpecifier(direction, wine.tannin)
                    "score" -> OrderSpecifier(direction, wine.score)
                    "price" -> OrderSpecifier(direction, wine.price)
                    else -> null
                }
            }
        }
        return null
    }

    private fun wineNameSearch(keyword: String?): BooleanExpression? =
    keyword?.let{
        wine.name.korName.contains(keyword).or(
            wine.name.engName.lower().contains(keyword.lowercase())
        )
    }

    private fun alcoholRangeBetween(minAlcohol: Double?, maxAlcohol: Double?): BooleanExpression? {
        return if (minAlcohol != null && maxAlcohol != null) {
            wine.alcohol.between(minAlcohol, maxAlcohol)
        } else if (minAlcohol != null) {
            wine.alcohol.goe(minAlcohol)
        } else if (maxAlcohol != null) {
            wine.alcohol.loe(maxAlcohol)
        } else {
            null
        }
    }

    private fun priceRangeBetween(minPrice: Int?, maxPrice: Int?): BooleanExpression? {
        return if (minPrice != null && maxPrice != null) {
            wine.price.between(minPrice, maxPrice)
        } else if (minPrice != null) {
            wine.price.goe(minPrice)
        } else if (maxPrice != null) {
            wine.price.loe(maxPrice)
        } else {
            null
        }
    }

    private fun wineTypeEq(wineType: Int?): BooleanExpression? {
        return if (wineType != null) wine.type.eq(WineType.values().find { it.value == wineType }) else null
    }

    private fun wineStyleEq(wineStyle: String?): BooleanExpression? {
        return if (wineStyle != null) wine.style.eq(wineStyle) else null
    }

    private fun wineGradeEq(wineGrade: String?): BooleanExpression? {
        return if (wineGrade != null) wine.grade.eq(wineGrade) else null
    }

    private fun regionEq(wineRegionId: Long?): BooleanExpression? {
        return if (wineRegionId != null) wine.region.id.eq(wineRegionId) else null
    }

    override fun getImportersCount(keyword: String?): Long? {
        return query.select(wine.importer.countDistinct())
            .from(wine)
            .where(
                importerNameSearch(keyword)
            )
            .fetchOne()
    }
    override fun getImporters(pageable: Pageable, keyword: String?): Slice<String> {
        val result = query.select(wine.importer)
            .from(wine)
            .where(
                importerNameSearch(keyword),
            )
            .distinct()
            .limit(pageable.pageSize.toLong() + 1)
            .offset(pageable.offset)
            .orderBy(wine.importer.asc())
            .fetch()

        return checkEndPage(pageable,result)
    }

    private fun importerNameSearch(keyword: String?): BooleanExpression? =
        keyword?.let {
            wine.importer.contains(keyword)
        }


}
