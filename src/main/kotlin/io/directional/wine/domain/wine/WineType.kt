package io.directional.wine.domain.wine

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

enum class WineType(val value:Int) {
    RED(1),
    SPARKLING(2),
    WHITE(3)
}

@Converter
class WineTypeConverter : AttributeConverter<WineType, Int> {
    override fun convertToDatabaseColumn(attribute: WineType?) = attribute?.value
    override fun convertToEntityAttribute(dbData: Int?) = WineType.values().find{it.value == dbData}
}
