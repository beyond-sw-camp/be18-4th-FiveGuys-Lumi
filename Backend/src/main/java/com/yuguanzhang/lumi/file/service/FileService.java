package com.yuguanzhang.lumi.file.service;

import com.yuguanzhang.lumi.file.dto.FileDownloadDto;
import com.yuguanzhang.lumi.file.dto.FileUploadResponseDto;
import com.yuguanzhang.lumi.file.enums.EntityType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    FileUploadResponseDto uploadFile(EntityType domain, MultipartFile file) throws IOException;

    FileDownloadDto downloadFile(Long fileId);

    void deleteFile(Long fileId);

}
