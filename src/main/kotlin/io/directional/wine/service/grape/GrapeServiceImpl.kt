package io.directional.wine.service.grape

import io.directional.wine.common.util.findByIdOrThrow
import io.directional.wine.domain.grape.Grape
import io.directional.wine.domain.grape.repository.GrapeRepository
import io.directional.wine.domain.grapeshare.GrapeShare
import io.directional.wine.domain.grapeshare.repository.GrapeShareRepository
import io.directional.wine.domain.region.repository.RegionRepository
import io.directional.wine.domain.winegrape.repository.WineGrapeRepository
import io.directional.wine.common.dto.ListPageResponse
import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.dto.grape.request.GrapeCreateRequest
import io.directional.wine.dto.grape.request.GrapeUpdateRequest
import io.directional.wine.dto.grape.request.ShareRequest
import io.directional.wine.dto.grape.response.GrapeCreateResponse
import io.directional.wine.dto.grape.response.GrapeDetailResponse
import io.directional.wine.dto.grape.response.GrapeListResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GrapeServiceImpl(
    private val grapeShareRepository: GrapeShareRepository,
    private val regionRepository: RegionRepository,
    private val grapeRepository: GrapeRepository,
    private val wineGrapeRepository: WineGrapeRepository,
):GrapeService {

    override fun insertGrape(request: GrapeCreateRequest): ResultResponse<GrapeCreateResponse> {
        val grapeShares = createGrapeShares(request.shares)
        val newGrape = Grape.createGrape(
            korName = request.korName,
            engName = request.engName,
            acidity = request.acidity,
            body = request.body,
            sweetness = request.sweetness,
            tannin = request.tannin,
            grapeShares = grapeShares,
        )
        return ResultResponse(result = GrapeCreateResponse.of(grapeRepository.save(newGrape)))
    }

    private fun createGrapeShares(shares: List<ShareRequest>, grape: Grape? = null): List<GrapeShare> =
        shares.map { info ->
            val region = regionRepository.findByIdOrThrow(info.regionId)
            GrapeShare.createGrapeShare(share = info.share, region = region, grape = grape)
        }

    override fun updateGrape(request: GrapeUpdateRequest, grapeId: Long) {
        grapeRepository.findByIdOrThrow(grapeId).apply {
            this.updateGrape(
                korName = request.korName,
                engName = request.engName,
                acidity = request.acidity,
                body = request.body,
                sweetness = request.sweetness,
                tannin = request.tannin,
            )
            // GrapeShare 수정
            updateGrapeShare(request, this)
        }
    }

    private fun updateGrapeShare(request: GrapeUpdateRequest, grape: Grape) {
        grapeShareRepository.deleteByGrapeId(grape.id)
        val grapeShares = createGrapeShares(request.shares, grape)
        grapeShareRepository.saveAll(grapeShares)
    }

    override fun deleteGrape(grapeId: Long) {
        grapeRepository.findByIdOrThrow(grapeId).apply {
            wineGrapeRepository.deleteByGrape(this)
            grapeShareRepository.deleteByGrapeId(this.id)
            grapeRepository.deleteById(this.id)
        }
    }

    @Transactional(readOnly = true)
    override fun getGrapes(pageable: Pageable, keyword: String?, filter: Long?, order: String, sort: String): ListPageResponse<GrapeListResponse> {
        // 동적 정렬 위한 pageable 객체 생성
        val orderPageable:Pageable = PageRequest.of(
            pageable.pageNumber, pageable.pageSize,
            Sort.by(Sort.Direction.fromString(sort), order)
        )
        val count = grapeRepository.getGrapesCount(keyword, filter)
        val response = grapeRepository.getGrapes(orderPageable, keyword, filter, order)

        return ListPageResponse(
            result = response.content.map(GrapeListResponse::of),
            totalCount = count,
            hasNext = response.hasNext(),
        )
    }

    @Transactional(readOnly = true)
    override fun getGrape(grapeId: Long): ResultResponse<GrapeDetailResponse> {
        val grape = grapeRepository.findByIdOrThrow(grapeId)
        val wineGrapes = wineGrapeRepository.findByGrape(grape)

        return ResultResponse(
            result = GrapeDetailResponse.of(grape, wineGrapes)
        )
    }
}
