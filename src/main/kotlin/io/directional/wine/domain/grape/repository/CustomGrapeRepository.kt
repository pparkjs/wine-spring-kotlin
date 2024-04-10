package io.directional.wine.domain.grape.repository

import io.directional.wine.domain.grape.Grape
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CustomGrapeRepository {
    fun getGrapesCount(keyword: String?, filter: Long?): Long?
    fun getGrapes(pageable: Pageable, keyword: String?, filter: Long?, order: String?): Slice<Grape>
}
