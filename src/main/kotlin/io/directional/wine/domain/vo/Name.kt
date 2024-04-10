package io.directional.wine.domain.vo

import io.directional.wine.common.exception.CustomException
import io.directional.wine.common.exception.ErrorCode
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Name(
    @Column(name = "name_korean", nullable = false)
    var korName: String,
    @Column(name = "name_english", nullable = false)
    var engName: String
){
    init {
        validateBlank(korName,engName)
        validateTrim(korName,engName)
        validateWord(korName,engName)
    }

    fun validateBlank(korName: String, engName: String) {
        if(korName.isBlank() || engName.isBlank()){
            throw CustomException(ErrorCode.NAME_LENGTH_EXCEPTION);
        }
    }

    fun validateTrim(korName: String, engName: String) {
        if (korName.startsWith(" ") || korName.endsWith(" ") ||
            engName.startsWith(" ") || engName.endsWith(" ")) {
            throw CustomException(ErrorCode.NAME_CONTAINS_WHITESPACE_EXCEPTION)
        }
    }

    fun validateWord(korName: String, engName: String) {
        val korPattern = Regex("^[ㄱ-ㅎ가-힣\\s\\p{Punct}\\d]*\$")
        val engPattern = Regex("^[a-zA-Zô\\s\\p{Punct}\\d]*\$")

        if (!korPattern.matches(korName)) {
            throw CustomException(ErrorCode.KOREAN_NAME_CONTAINS_ENGLISH_EXCEPTION)
        }

        if (!engPattern.matches(engName)) {
            throw CustomException(ErrorCode.ENGLISH_NAME_CONTAINS_KOREAN_EXCEPTION)
        }
    }
}
