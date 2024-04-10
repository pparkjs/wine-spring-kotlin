package io.directional.wine.domain.winery.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import io.directional.wine.common.util.checkEndPage
import io.directional.wine.domain.region.QRegion.region
import io.directional.wine.domain.winery.QWinery.*
import io.directional.wine.domain.winery.Winery
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class CustomWineryRepositoryImpl(
    private val query: JPAQueryFactory
): CustomWineryRepository {
    override fun getWineriesCount(keyword: String?, filter: Long?): Long? {
        return query.select(winery.count())
            .from(winery)
            .where(
                wineryNameSearch(keyword),
                regionFilter(filter),
            )
            .fetchOne()
    }

    override fun getWineries(keyword: String?, filter: Long?, pageable: Pageable): Slice<Winery> {
        val result = query.selectFrom(winery)
            .leftJoin(winery.region, region).fetchJoin()
            .where(
                wineryNameSearch(keyword),
                regionFilter(filter),
            )
            .limit(pageable.pageSize.toLong() + 1)
            .offset(pageable.offset)
            .orderBy(winery.name.engName.asc())
            .fetch()

        return checkEndPage(pageable,result)
    }

    private fun regionFilter(filter: Long?): BooleanExpression? =
        filter?.let {
            winery.region.id.eq(filter)
        }

    private fun wineryNameSearch(keyword: String?): BooleanExpression? =
        keyword?.let{
            winery.name.korName.contains(keyword).or(
                winery.name.engName.lower().contains(keyword.lowercase())
            )
        }
}
