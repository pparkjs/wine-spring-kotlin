package io.directional.wine.controller

import io.directional.wine.dto.winery.request.WineryCreateRequest
import io.directional.wine.dto.winery.request.WineryUpdateRequest
import io.directional.wine.service.winery.WineryService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/winery")
class WineryController(
    private val wineryService: WineryService
) {

    @PostMapping
    fun insertWinery(@RequestBody request: WineryCreateRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(
            wineryService.insertWinery(
                korName = request.korName,
                engName = request.engName,
                regionId = request.regionId,
            )
        )

    @PatchMapping("/{wineryId}")
    fun updateWinery(@RequestBody request: WineryUpdateRequest, @PathVariable wineryId:Long) =
        ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            wineryService.updateWinery(
                korName = request.korName,
                engName = request.engName,
                wineryId = wineryId,
            )
        )

    @DeleteMapping("/{wineryId}")
    fun deleteWinery(@PathVariable wineryId:Long) =
        ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            wineryService.deleteWinery(
                wineryId = wineryId,
            )
        )

    @GetMapping
    fun getWineries(
        @RequestParam(name = "keyword", required = false) keyword:String?, // 와이너리이름
        @RequestParam(name = "filter", required = false) filter:Long?, // 지역
        @PageableDefault(size = 10) pageable: Pageable   // param ex) -> page=0, size=10
    ) =
        ResponseEntity.status(HttpStatus.OK).body(
            wineryService.getWineries(
                pageable = pageable,
                keyword = keyword,
                filter = filter,
            )
        )

    @GetMapping("/{wineryId}")
    fun getWinery(@PathVariable wineryId:Long) =
        ResponseEntity.status(HttpStatus.OK).body(
            wineryService.getWinery(wineryId = wineryId)
        )
}
