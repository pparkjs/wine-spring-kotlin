package io.directional.wine.domain.region.repository

import io.directional.wine.domain.region.Region
import org.springframework.data.jpa.repository.JpaRepository

interface RegionRepository : JpaRepository<Region, Long>, CustomRegionRepository
