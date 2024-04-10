package io.directional.wine.domain.grape.repository

import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import io.directional.wine.common.util.checkEndPage
import io.directional.wine.domain.grape.Grape
import io.directional.wine.domain.grape.QGrape.grape
import io.directional.wine.domain.grapeshare.QGrapeShare.grapeShare
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class CustomGrapeRepositoryImpl(
    private val query: JPAQueryFactory
): CustomGrapeRepository {
    override fun getGrapesCount(keyword: String?, filter: Long?): Long? {
        return query.select(grape.countDistinct())
            .from(grape)
            .join(grapeShare).on(grape.id.eq(grapeShare.grape.id))
            .where(
                grapeNameSearch(keyword),
                regionFilter(filter),
            )
            .fetchOne()
    }

    override fun getGrapes(pageable: Pageable, keyword: String?, filter: Long?, order: String?): Slice<Grape> {
        val orderSpecifiers = createOrderSpecifier(pageable)

        val result = query.selectFrom(grape)
            .join(grape.mutableGrapeShares, grapeShare)
            .where(
                grapeNameSearch(keyword),
                regionFilter(filter),
            )
            .distinct()
            .limit(pageable.pageSize.toLong() + 1)
            .offset(pageable.offset)
            .orderBy(orderSpecifiers)
            .fetch()

        return checkEndPage(pageable,result)
    }

    private fun createOrderSpecifier(pageable: Pageable): OrderSpecifier<*>? {
        if(!pageable.sort.isEmpty){
            pageable.sort.forEach { order ->
                val direction = if (order.isAscending) Order.ASC else Order.DESC

                return when (order.property){
                    "grape" -> OrderSpecifier(direction, grape.name.engName)
                    "acidity" -> OrderSpecifier(direction, grape.acidity)
                    "body" -> OrderSpecifier(direction, grape.body)
                    "sweetness" -> OrderSpecifier(direction, grape.sweetness)
                    "tannin" -> OrderSpecifier(direction, grape.tannin)
                    else -> null
                }
            }
        }
        return null
    }

    private fun regionFilter(filter: Long?): BooleanExpression? =
        filter?.let {
            grapeShare.region.id.eq(filter)
        }

    private fun grapeNameSearch(keyword: String?): BooleanExpression? =
        keyword?.let{
            grape.name.korName.contains(keyword).or(
                grape.name.engName.lower().contains(keyword.lowercase())
            )
        }
}
