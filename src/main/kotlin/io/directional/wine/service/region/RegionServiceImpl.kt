package io.directional.wine.service.region

import io.directional.wine.common.util.findByIdOrThrow
import io.directional.wine.domain.grapeshare.repository.GrapeShareRepository
import io.directional.wine.domain.region.Region
import io.directional.wine.domain.region.repository.RegionRepository
import io.directional.wine.domain.wine.repository.WineRepository
import io.directional.wine.domain.winery.repository.WineryRepository
import io.directional.wine.common.dto.ListPageResponse
import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.dto.region.response.RegionCreateResponse
import io.directional.wine.dto.region.response.RegionDetailResponse
import io.directional.wine.dto.region.response.RegionListResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RegionServiceImpl(
    private val regionRepository: RegionRepository,
    private val grapeShareRepository: GrapeShareRepository,
    private val wineryRepository: WineryRepository,
    private val wineRepository: WineRepository,
):RegionService {
    override fun insertRegion(korName: String, engName: String, parentId: Long?): ResultResponse<RegionCreateResponse> {
        val parentRegion = parentId?.let{
            regionRepository.findByIdOrThrow(parentId)
        }

        val newRegion = Region.createRegion(
            korName = korName,
            engName = engName,
            parent = parentRegion,
            rootId = parentRegion?.rootId?:parentRegion?.id
        ).apply {
            this.updateDepth()
        }

        return ResultResponse(
            result = RegionCreateResponse.of(
                regionRepository.save(newRegion)
            )
        )
    }

    override fun updateRegion(korName: String, engName: String, regionId: Long) {
        regionRepository.findByIdOrThrow(regionId).also {
            it.updateName(korName, engName)
        }
    }

    override fun deleteRegion(regionId: Long) {
        regionRepository.findByIdOrThrow(regionId).apply {
            wineRepository.deleteByRegion(this)
            wineryRepository.deleteByRegion(this)
            grapeShareRepository.deleteByRegion(this)
            regionRepository.deleteById(regionId)
        }
    }

    @Transactional(readOnly = true)
    override fun getRegions(pageable: Pageable, keyword: String?, filter: Long?): ListPageResponse<RegionListResponse> {
        val count = regionRepository.getRegionsCount(keyword, filter)
        val response = regionRepository.getRegions(pageable, keyword, filter)

        return ListPageResponse(
            result = response.content.map(RegionListResponse::of),
            totalCount = count,
            hasNext = response.hasNext()
        )
    }

    @Transactional(readOnly = true)
    override fun getRegion(regionId: Long): ResultResponse<RegionDetailResponse> =
        regionRepository.findByIdOrThrow(regionId).run{
            val shareGrapes = regionRepository.getGrapeShareByRegion(regionId)
            val wineries = regionRepository.getWineryByRegion(regionId)
            val wines = regionRepository.getWineByRegion(regionId)
            val parentRegions = regionRepository.getAllParentRegion(regionId)
            val detailResponse = RegionDetailResponse.of(
                region = this,
                parentRegions = parentRegions,
                grapes = shareGrapes,
                wineries = wineries,
                wines = wines,
            )
            return ResultResponse(result = detailResponse)
        }
}
