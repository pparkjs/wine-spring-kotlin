package io.directional.wine.domain.grape.repository

import io.directional.wine.domain.grape.Grape
import org.springframework.data.jpa.repository.JpaRepository

interface GrapeRepository : JpaRepository<Grape, Long>, CustomGrapeRepository
