package io.directional.wine.controller

import io.directional.wine.dto.region.request.RegionCreateRequest
import io.directional.wine.dto.region.request.RegionUpdateRequest
import io.directional.wine.service.region.RegionService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/region")
class RegionController(
    private val regionService: RegionService
) {

    @PostMapping
    fun insertRegion(@RequestBody request: RegionCreateRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(
            regionService.insertRegion(
                korName = request.korName,
                engName = request.engName,
                parentId = request.parentId,
            )
        )

    @PatchMapping("/{regionId}")
    fun updateRegion(@RequestBody request: RegionUpdateRequest, @PathVariable regionId:Long) =
        ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            regionService.updateRegion(
                korName = request.korName,
                engName = request.engName,
                regionId = regionId,
            )
        )

    @DeleteMapping("/{regionId}")
    fun deleteRegion(@PathVariable regionId:Long) =
        ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            regionService.deleteRegion(
                regionId = regionId,
            )
        )

    @GetMapping
    fun getRegions(
        @RequestParam(name = "keyword", required = false) keyword:String?, // 지역 이름
        @RequestParam(name = "filter", required = false) filter:Long?, // 상위 지역
        @PageableDefault(size = 10) pageable: Pageable   // param -> page, size required
    ) =
        ResponseEntity.status(HttpStatus.OK).body(
            regionService.getRegions(
                pageable = pageable,
                keyword = keyword,
                filter = filter
            )
        )

    @GetMapping("/{regionId}")
    fun getRegion(@PathVariable regionId:Long) =
        ResponseEntity.status(HttpStatus.OK).body(
            regionService.getRegion(regionId = regionId)
        )
}
