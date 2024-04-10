package io.directional.wine.service.wine

import io.directional.wine.common.util.findByIdOrThrow
import io.directional.wine.domain.grape.repository.GrapeRepository
import io.directional.wine.domain.region.repository.RegionRepository
import io.directional.wine.domain.wine.Wine
import io.directional.wine.domain.wine.repository.WineRepository
import io.directional.wine.domain.winearoma.WineAroma
import io.directional.wine.domain.winearoma.repository.WineAromaRepository
import io.directional.wine.domain.winegrape.WineGrape
import io.directional.wine.domain.winegrape.repository.WineGrapeRepository
import io.directional.wine.domain.winepairing.WinePairing
import io.directional.wine.domain.winepairing.repository.WinePairingRepository
import io.directional.wine.domain.winery.repository.WineryRepository
import io.directional.wine.common.dto.ListPageResponse
import io.directional.wine.common.dto.ResultResponse
import io.directional.wine.dto.wine.request.WineCreateRequest
import io.directional.wine.dto.wine.request.WineFilter
import io.directional.wine.dto.wine.request.WineUpdateRequest
import io.directional.wine.dto.wine.response.ImporterDetailResponse
import io.directional.wine.dto.wine.response.WineCreateResponse
import io.directional.wine.dto.wine.response.WineDetailResponse
import io.directional.wine.dto.wine.response.WineListResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WineServiceImpl(
    private val wineRepository: WineRepository,
    private val wineryRepository: WineryRepository,
    private val regionRepository: RegionRepository,
    private val grapeRepository: GrapeRepository,
    private val winePairingRepository: WinePairingRepository,
    private val wineGrapeRepository: WineGrapeRepository,
    private val wineAromaRepository: WineAromaRepository,
):WineService {

    override fun insertWine(request: WineCreateRequest): ResultResponse<WineCreateResponse> {
        val winery = wineryRepository.findByIdOrThrow(request.wineryId)
        val region = regionRepository.findByIdOrThrow(request.regionId)
        val wineGrape = createWineGrape(request.grapes)
        val wineAroma = createWineAroma(request.aromas)
        val winePairing = createWinePairing(request.pairings)

        val newWine = Wine.createWine(
            korName = request.korName, engName = request.engName, type = request.type,
            alcohol = request.alcohol, acidity = request.acidity, body = request.body,
            sweetness = request.sweetness, tannin = request.tannin,servingTemperature = request.servingTemperature,
            score = request.score, price = request.price, style = request.style,
            grade = request.grade, importer = request.importer, winery = winery,
            region = region, wineGrape = wineGrape, wineAroma = wineAroma,
            winePairing = winePairing,
        )
        return ResultResponse(result = WineCreateResponse.of(wineRepository.save(newWine)))
    }

    private fun createWinePairing(pairings: List<String>, wine:Wine? = null): List<WinePairing> =
        pairings.map{pairing -> WinePairing.createWinePairing(pairing = pairing, wine = wine)}

    private fun createWineAroma(aromas: List<String>, wine:Wine? = null): List<WineAroma> =
        aromas.map{aroma -> WineAroma.createWineAroma(aroma = aroma, wine = wine)}

    private fun createWineGrape(grapes: List<Long>, wine:Wine? = null): List<WineGrape> =
        grapes.map(grapeRepository::findByIdOrThrow)
              .map{grape -> WineGrape.createWineGrape(grape = grape, wine = wine)}

    override fun updateWine(request: WineUpdateRequest, wineId: Long) {
        wineRepository.findByIdOrThrow(wineId).apply {
            this.updateWine(
                korName = request.korName, engName = request.engName, type = request.type,
                alcohol = request.alcohol, acidity = request.acidity, body = request.body,
                sweetness = request.sweetness, tannin = request.tannin,servingTemperature = request.servingTemperature,
                score = request.score, price = request.price, style = request.style,
                grade = request.grade, importer = request.importer,
                winery = wineryRepository.findByIdOrThrow(request.wineryId),
                region = regionRepository.findByIdOrThrow(request.regionId),
            )
            updateWinePairing(request, this)
            updateWineAroma(request, this)
            updateWineGrape(request, this)
        }
    }

    fun updateWineGrape(request: WineUpdateRequest, wine: Wine) {
        wineGrapeRepository.deleteByWineId(wine.id)
        val wineGrape = createWineGrape(request.grapes, wine)
        wineGrapeRepository.saveAll(wineGrape)
    }

    fun updateWineAroma(request: WineUpdateRequest, wine: Wine) {
        wineAromaRepository.deleteByWineId(wine.id)
        val wineAroma = createWineAroma(request.aromas, wine)
        wineAromaRepository.saveAll(wineAroma)
    }

    fun updateWinePairing(request: WineUpdateRequest, wine: Wine) {
        winePairingRepository.deleteByWineId(wine.id)
        val winePairing = createWinePairing(request.pairings, wine)
        winePairingRepository.saveAll(winePairing)
    }

    override fun deleteWine(wineId: Long) {
        wineRepository.findByIdOrThrow(wineId).apply {
            wineAromaRepository.deleteByWineId(this.id)
            wineGrapeRepository.deleteByWineId(this.id)
            winePairingRepository.deleteByWineId(this.id)
            wineRepository.deleteById(this.id)
        }
    }

    @Transactional(readOnly = true)
    override fun getWines(pageable: Pageable, keyword: String?, filter: WineFilter, order: String, sort: String): ListPageResponse<WineListResponse> {
        // 동적 정렬 위한 pageable 객체 생성
        val orderPageable:Pageable = PageRequest.of(
            pageable.pageNumber, pageable.pageSize,
            Sort.by(Sort.Direction.fromString(sort), order)
        )
        val count = wineRepository.getWinesCount(keyword, filter)
        val response = wineRepository.getWines(orderPageable, keyword, filter, order)

        return ListPageResponse(
            result = response.content,
            totalCount = count,
            hasNext = response.hasNext(),
        )
    }

    @Transactional(readOnly = true)
    override fun getGrape(wineId: Long): ResultResponse<WineDetailResponse> {
        val wine = wineRepository.findByIdOrThrow(wineId)
        val parentRegions = regionRepository.getAllParentRegion(wine.region.id)
        return ResultResponse(
            result = WineDetailResponse.of(wine, parentRegions)
        )
    }

    override fun getImporters(pageable: Pageable, keyword: String?): ListPageResponse<String> {
        val count = wineRepository.getImportersCount(keyword)
        val response = wineRepository.getImporters(pageable, keyword)

        return ListPageResponse(
            result = response.content,
            totalCount = count,
            hasNext = response.hasNext(),
        )
    }

    override fun getImporter(importerName: String): ResultResponse<ImporterDetailResponse> {
        val wineNames = wineRepository.findByImporter(importerName)
        return ResultResponse(
            result = ImporterDetailResponse.of(importerName, wineNames)
        )
    }
}
