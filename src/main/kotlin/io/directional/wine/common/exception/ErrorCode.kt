package io.directional.wine.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND

enum class ErrorCode(val status: HttpStatus, val message: String) {

    RESOURCE_NOT_FOUND(NOT_FOUND, "대상이 존재하지 않습니다."),
    NAME_CONTAINS_WHITESPACE_EXCEPTION(BAD_REQUEST, "이름의 처음과 마지막에 공백이 존재할 수 없습니다."),
    NAME_LENGTH_EXCEPTION(BAD_REQUEST, "이름은 1글자 이상 입력해야합니다."),
    KOREAN_NAME_CONTAINS_ENGLISH_EXCEPTION(BAD_REQUEST, "한글 이름에는 영어를 입력할 수 없습니다."),
    ENGLISH_NAME_CONTAINS_KOREAN_EXCEPTION(BAD_REQUEST, "영어 이름에는 한글를 입력할 수 없습니다."),
}
