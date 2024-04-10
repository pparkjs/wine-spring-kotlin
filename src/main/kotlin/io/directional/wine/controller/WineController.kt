package io.directional.wine.controller

import io.directional.wine.dto.wine.request.WineCreateRequest
import io.directional.wine.dto.wine.request.WineFilter
import io.directional.wine.dto.wine.request.WineUpdateRequest
import io.directional.wine.service.wine.WineService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/wine")
class WineController(
    private val wineService: WineService,
) {
    @PostMapping
    fun insertWine(@RequestBody request: WineCreateRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(
            wineService.insertWine(request = request)
        )

    @PatchMapping("/{wineId}")
    fun updateWine(@RequestBody request: WineUpdateRequest, @PathVariable wineId:Long) =
        ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            wineService.updateWine(request = request, wineId = wineId)
        )
    @DeleteMapping("/{wineId}")
    fun deleteWine(@PathVariable wineId:Long) =
        ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            wineService.deleteWine(wineId = wineId)
        )

    @GetMapping
    fun getWines(
        @RequestParam(name = "keyword", required = false) keyword:String?, // 와인이름
        @RequestParam(name = "order", defaultValue = "wine") order:String, // wine,alcohol,acidity,body,sweetness,tannin, score, price
        @RequestParam(name = "sort", defaultValue = "asc") sort :String, // asc, desc
        @PageableDefault(size = 10) pageable: Pageable,   // param ex) -> page=0, size=10
        @ModelAttribute filter: WineFilter,
    ) =
        ResponseEntity.status(HttpStatus.OK).body(
            wineService.getWines(
                pageable = pageable,
                keyword = keyword,
                filter = filter,
                order = order,
                sort = sort,
            )
        )

    @GetMapping("/{wineId}")
    fun getWine(@PathVariable wineId:Long) =
        ResponseEntity.status(HttpStatus.OK).body(
            wineService.getGrape(wineId = wineId)
        )

    @GetMapping("/importer")
    fun getImporters(
        @RequestParam(name = "keyword", required = false) keyword:String?, // 수입사 이름
        @PageableDefault(size = 10) pageable: Pageable,   // param ex) -> page=0, size=10
    ) =
        ResponseEntity.status(HttpStatus.OK).body(
            wineService.getImporters(
                pageable = pageable,
                keyword = keyword,
            )
        )

    @GetMapping("/importer/{importerName}")
    fun getWine(@PathVariable importerName:String) =
        ResponseEntity.status(HttpStatus.OK).body(
            wineService.getImporter(importerName = importerName)
        )
}
