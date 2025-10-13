package com.yuguanzhang.lumi.file.controller;

import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.file.dto.FileDownloadDto;
import com.yuguanzhang.lumi.file.dto.FileUploadResponseDto;
import com.yuguanzhang.lumi.file.enums.EntityType;
import com.yuguanzhang.lumi.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class FileController {
    private final FileService fileService;

    @PostMapping("/{domain}/files/upload")
    public ResponseEntity<BaseResponseDto<FileUploadResponseDto>> uploadFile(
            @PathVariable("domain") EntityType domain, @RequestPart("file") MultipartFile file)
            throws IOException {

        FileUploadResponseDto fileUpload = fileService.uploadFile(domain, file);

        BaseResponseDto<FileUploadResponseDto> response =
                BaseResponseDto.of(HttpStatus.CREATED, fileUpload);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/files/{file_id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable("file_id") Long fileId) {
        FileDownloadDto fileDownloadDto = fileService.downloadFile(fileId);

        List<String> allowedTypes =
                List.of("image/png", "image/jpeg", "application/pdf", "application/zip");

        if (!allowedTypes.contains(fileDownloadDto.getContentType())) {
            throw new IllegalArgumentException(
                    "허용되지 않은 파일 타입입니다: " + fileDownloadDto.getContentType());
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDownloadDto.getContentType())) // 파일 타입
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment() // 다운로드창 표시
                        .filename(fileDownloadDto.getOriginalFileName(), StandardCharsets.UTF_8)
                        .build().toString()).contentLength(fileDownloadDto.getContentLength())
                .body(fileDownloadDto.getResource());
    }


    @DeleteMapping("/files/{file_id}")
    public ResponseEntity<BaseResponseDto<Void>> deleteFile(@PathVariable("file_id") Long fileId) {
        fileService.deleteFile(fileId);

        BaseResponseDto<Void> response = BaseResponseDto.of(HttpStatus.OK, null);

        return ResponseEntity.ok(response);
    }
}


