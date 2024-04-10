package io.directional.wine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WineApplication

fun main(args: Array<String>) {
    runApplication<WineApplication>(*args)
}
