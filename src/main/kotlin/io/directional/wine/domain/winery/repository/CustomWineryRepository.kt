package io.directional.wine.domain.winery.repository

import io.directional.wine.domain.winery.Winery
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CustomWineryRepository {
    fun getWineriesCount(keyword: String?, filter: Long?): Long?
    fun getWineries(keyword: String?, filter: Long?, pageable: Pageable): Slice<Winery>
}
