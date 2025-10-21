package com.yuguanzhang.lumi.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.Resource;

@Builder
@Getter
@AllArgsConstructor
public class FileDownloadDto {
    Resource resource;
    String originalFileName;
    Long contentLength;
    String contentType;
}
