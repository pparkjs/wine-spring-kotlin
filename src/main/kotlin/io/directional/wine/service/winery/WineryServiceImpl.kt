package io.directional.wine.service.winery

import io.directional.wine.common.util.findByIdOrThrow
import io.directional.wine.domain.region.repository.RegionRepository
import io.directional.wine.domain.wine.repository.WineRepository
import io.directional.wine.domain.winery.Winery
import io.directional.wine.domain.winery.repository.WineryRepository
import io.directional.wine.common.dto.ListPageResponse
import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.dto.winery.response.WineryCreateResponse
import io.directional.wine.dto.winery.response.WineryDetailResponse
import io.directional.wine.dto.winery.response.WineryListResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WineryServiceImpl(
    private val wineryRepository: WineryRepository,
    private val wineRepository: WineRepository,
    private val regionRepository: RegionRepository
):WineryService {

    override fun insertWinery(korName: String, engName: String, regionId: Long): ResultResponse<WineryCreateResponse> =
        regionRepository.findByIdOrThrow(regionId).run {
            ResultResponse(
                result = WineryCreateResponse.of(
                    wineryRepository.save(Winery.createWinery(korName = korName, engName = engName, this))
                )
            )
        }

    override fun updateWinery(korName: String, engName: String, wineryId: Long) {
        wineryRepository.findByIdOrThrow(wineryId).also {
            it.updateName(korName, engName)
        }
    }

    override fun deleteWinery(wineryId: Long) {
        wineryRepository.findByIdOrThrow(wineryId).apply {
            wineRepository.deleteByWinery(this)
            wineryRepository.deleteById(wineryId)
        }
    }

    @Transactional(readOnly = true)
    override fun getWineries(pageable: Pageable, keyword: String?, filter: Long?): ListPageResponse<WineryListResponse> {
        val count = wineryRepository.getWineriesCount(keyword, filter)
        val response = wineryRepository.getWineries(keyword, filter, pageable)

        return ListPageResponse(
            result = response.content.map(WineryListResponse::of),
            totalCount = count,
            hasNext = response.hasNext(),
        )
    }

    @Transactional(readOnly = true)
    override fun getWinery(wineryId: Long): ResultResponse<WineryDetailResponse> {
        // 단일 조회 시: 와이너리 이름, 지역 이름, 와이너리의 와인
        val winery = wineryRepository.findByIdOrThrow(wineryId)
        val wineNames = wineRepository.findByWinery(winery)

        return ResultResponse(
            result = WineryDetailResponse.of(winery, wineNames)
        )
    }
}
