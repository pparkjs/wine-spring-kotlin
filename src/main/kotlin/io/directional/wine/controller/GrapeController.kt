package io.directional.wine.controller

import io.directional.wine.dto.grape.request.GrapeCreateRequest
import io.directional.wine.dto.grape.request.GrapeUpdateRequest
import io.directional.wine.service.grape.GrapeService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/grape")
class GrapeController(
    private val grapeService: GrapeService
) {

    @PostMapping
    fun insertGrape(@RequestBody request: GrapeCreateRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(
            grapeService.insertGrape(request = request)
        )

    @PatchMapping("/{grapeId}")
    fun updateGrape(@RequestBody request: GrapeUpdateRequest, @PathVariable grapeId:Long) =
        ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            grapeService.updateGrape(request = request, grapeId = grapeId)
        )

    @DeleteMapping("/{grapeId}")
    fun deleteGrape(@PathVariable grapeId:Long) =
        ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            grapeService.deleteGrape(grapeId = grapeId)
        )

    @GetMapping
    fun getGrapes(
        @RequestParam(name = "keyword", required = false) keyword:String?, // 포도품종이름
        @RequestParam(name = "order", defaultValue = "grape") order:String, // grape,acidity,body,sweetness,tannin
        @RequestParam(name = "sort", defaultValue = "asc") sort :String, // asc, desc
        @RequestParam(name = "filter", required = false) filter:Long?, // 지역
        @PageableDefault(size = 10) pageable: Pageable   // param ex) -> page=0, size=10
    ) =
        ResponseEntity.status(HttpStatus.OK).body(
            grapeService.getGrapes(
                pageable = pageable,
                keyword = keyword,
                filter = filter,
                order = order,
                sort = sort,
            )
        )

    @GetMapping("/{grapeId}")
    fun getGrape(@PathVariable grapeId:Long) =
        ResponseEntity.status(HttpStatus.OK).body(
            grapeService.getGrape(grapeId = grapeId)
        )
}
