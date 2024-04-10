package io.directional.wine.domain.region.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import io.directional.wine.domain.region.QRegion.region
import com.querydsl.jpa.impl.JPAQueryFactory
import io.directional.wine.common.util.checkEndPage
import io.directional.wine.domain.grape.QGrape.grape
import io.directional.wine.domain.grapeshare.GrapeShare
import io.directional.wine.domain.grapeshare.QGrapeShare.grapeShare
import io.directional.wine.domain.region.QRegion
import io.directional.wine.domain.region.Region
import io.directional.wine.domain.wine.QWine.wine
import io.directional.wine.domain.wine.Wine
import io.directional.wine.domain.winery.QWinery.winery
import io.directional.wine.domain.winery.Winery
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class CustomRegionRepositoryImpl(
    private val query: JPAQueryFactory
): CustomRegionRepository {

    override fun getRegionsCount(keyword: String?, filter: Long?): Long? {
        return query.select(region.count())
            .from(region)
            .where(
                regionNameSearch(keyword),
                parentRegionFilter(filter),
            )
            .fetchOne()
    }

    override fun getRegions(pageable: Pageable, keyword: String?, filter: Long?): Slice<Region> {
        val result = query.select(region)
            .from(region)
            .where(
                regionNameSearch(keyword),
                parentRegionFilter(filter),
            )
            .orderBy(region.name.engName.asc())
            .limit(pageable.pageSize.toLong() + 1)
            .offset(pageable.offset)
            .fetch()

        return checkEndPage(pageable,result)
    }

    private fun parentRegionFilter(filter: Long?): BooleanExpression? =
        filter?.let {
            region.parent.id.eq(filter)
        }

    private fun regionNameSearch(keyword: String?): BooleanExpression? =
        keyword?.let{
            region.name.korName.contains(keyword).or(
                region.name.engName.lower().contains(keyword.lowercase())
            )
        }

    override fun getGrapeShareByRegion(regionId: Long): List<GrapeShare> {
       val subRegion = QRegion("subRegion")
       return query.selectFrom(grapeShare)
            .join(grapeShare.grape, grape).fetchJoin()
            .where(
                grapeShare.region.id.eq(
                    JPAExpressions
                            .select(subRegion.rootId.coalesce(regionId))
                            .from(subRegion)
                            .where(subRegion.id.eq(regionId))
                )
            )
            .fetch()
    }

    override fun getWineryByRegion(regionId: Long): List<Winery> {
        val subRegion = QRegion("subRegion")
        return query.selectFrom(winery)
            .where(
                winery.region.id.eq(
                    JPAExpressions
                        .select(subRegion.rootId.coalesce(regionId))
                        .from(subRegion)
                        .where(subRegion.id.eq(regionId))
                )
            )
            .fetch()
    }

    override fun getWineByRegion(regionId: Long): List<Wine> {
        return query.selectFrom(wine)
            .join(wine.region, region).fetchJoin()
            .where(
                wine.region.id.eq(regionId)
            )
            .fetch()
    }

    override fun getAllParentRegion(regionId: Long): MutableList<Region> {
        val parents = mutableListOf<Region>()
        var currentRegionId = regionId

        while (currentRegionId != null) {
            val parent = query.select(region.parent)
                .from(region)
                .where(region.id.eq(currentRegionId))
                .fetchOne()

            if (parent != null) {
                parents.add(parent)
                currentRegionId = parent.id
            } else {
                break
            }
        }
        return parents
    }

}
