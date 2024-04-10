package io.directional.wine.acceptance.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.directional.wine.domain.grape.Grape
import io.directional.wine.domain.grape.repository.GrapeRepository
import io.directional.wine.domain.grapeshare.repository.GrapeShareRepository
import io.directional.wine.domain.region.Region
import io.directional.wine.domain.region.repository.RegionRepository
import io.directional.wine.domain.wine.Wine
import io.directional.wine.domain.wine.repository.WineRepository
import io.directional.wine.domain.winearoma.repository.WineAromaRepository
import io.directional.wine.domain.winegrape.repository.WineGrapeRepository
import io.directional.wine.domain.winepairing.repository.WinePairingRepository
import io.directional.wine.domain.winery.Winery
import io.directional.wine.domain.winery.repository.WineryRepository
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.jdbc.core.JdbcTemplate

@TestInstance(PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptanceTest {

    @LocalServerPort
    protected var port: Int = 0

    private var databaseCleaner: DatabaseCleanup? = null

    @Autowired
    protected lateinit var regionRepository: RegionRepository

    @Autowired
    protected lateinit var wineryRepository: WineryRepository

    @Autowired
    protected lateinit var grapeShareRepository: GrapeShareRepository

    @Autowired
    protected lateinit var grapeRepository: GrapeRepository

    @Autowired
    protected lateinit var wineRepository: WineRepository

    @Autowired
    protected lateinit var wineAromaRepository: WineAromaRepository

    @Autowired
    protected lateinit var wineGrapeRepository: WineGrapeRepository

    @Autowired
    protected lateinit var winePairingRepository: WinePairingRepository

    @BeforeAll
    fun beforeAll(@Autowired jdbcTemplate: JdbcTemplate) {
        databaseCleaner = DatabaseCleanup(jdbcTemplate)
    }

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        databaseCleaner?.clean()
    }

    companion object {
        val mapper: ObjectMapper = jacksonObjectMapper().registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
    }
    protected fun 지역을_저장한다(region: Region): Region {
        return regionRepository.save(region)
    }

    protected fun 와이너리를_저장한다(winery: Winery): Winery {
        return wineryRepository.save(winery)
    }

    protected fun 포도품종을_저장한다(grape: Grape): Grape {
        return grapeRepository.save(grape)
    }

    protected fun 와인을_저장한다(wine: Wine): Wine {
        return wineRepository.save(wine)
    }
}
