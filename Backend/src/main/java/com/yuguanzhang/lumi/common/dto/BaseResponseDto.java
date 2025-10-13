package com.yuguanzhang.lumi.common.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class BaseResponseDto<T> {

    private final int code;

    private final String message;

    private final List<T> data;

    // 단일 객체용
    public BaseResponseDto(HttpStatus status, T item) {
        this.code = status.value();
        this.message = status.getReasonPhrase();
        this.data = Collections.singletonList(item);
    }

    // 복수 객체용
    public BaseResponseDto(HttpStatus status, List<T> items) {
        this.code = status.value();
        this.message = status.getReasonPhrase();
        this.data = items;
    }

    // 에러 응답
    public BaseResponseDto(HttpStatus status, String errorMessage) {
        this.code = status.value();
        this.message = errorMessage;
        this.data = null;
    }

    //스테틱메소드가 있으면 new 선언하면서 안해도 되고, 타입도 신경 안써도 된다고 해서 넣었습니다.
    // static 팩토리 메소드
    public static <T> BaseResponseDto<T> of(HttpStatus status, T item) {
        return new BaseResponseDto<>(status, item);
    }

    // 복수 객체용 static 메소드
    public static <T> BaseResponseDto<T> of(HttpStatus status, List<T> items) {
        return new BaseResponseDto<>(status, items);
    }

    // 에러용 static 메소드
    public static <T> BaseResponseDto<T> error(HttpStatus status, String errorMessage) {
        return new BaseResponseDto<>(status, errorMessage);
    }
}
