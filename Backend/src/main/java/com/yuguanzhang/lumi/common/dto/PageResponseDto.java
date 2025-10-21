package com.yuguanzhang.lumi.common.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class PageResponseDto<T> extends BaseResponseDto<T> {
    private final int page;

    private final int numOfRows;

    private final long totalCount;

    private final int totalPages;

    public PageResponseDto(HttpStatus status, Page<T> pageData) {
        super(status, pageData.getContent());
        this.page = pageData.getNumber();
        this.numOfRows = pageData.getSize();
        this.totalCount = pageData.getTotalElements();
        this.totalPages = pageData.getTotalPages();
    }

    public static <T> PageResponseDto<T> page(HttpStatus status, Page<T> pageData) {

        return new PageResponseDto<>(status, pageData);
    }
}
