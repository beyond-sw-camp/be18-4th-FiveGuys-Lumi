package com.yuguanzhang.lumi.file.service;

import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.file.dto.FileDownloadDto;
import com.yuguanzhang.lumi.file.vo.FilePathInfo;
import com.yuguanzhang.lumi.file.dto.FileUploadResponseDto;
import com.yuguanzhang.lumi.file.entity.File;
import com.yuguanzhang.lumi.file.enums.EntityType;
import com.yuguanzhang.lumi.file.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private final String uploadDir; // 업로드 경로
    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository,
                           @Value("${file.upload-dir}") String uploadDir) {
        this.uploadDir = uploadDir;
        this.fileRepository = fileRepository;
    }

    // 단일 파일 저장 후 FileUpload Dto 반환
    @Override
    public FileUploadResponseDto uploadFile(EntityType domain, MultipartFile multipartFile)
            throws IOException {
        // 1. 새로운 파일명 생성
        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID()
                          .toString();
        String newFileName = uuid + "-" + originalFileName;

        // 2. 파일을 서버 로컬 디렉토리에 저장
        FilePathInfo copyofLocation = generateFilePath(domain, newFileName);

        log.info("copyofLocation: {}", copyofLocation);

        Files.copy(multipartFile.getInputStream(), copyofLocation.getFullPath());

        // 3. DB에 저장
        File file = File.builder()
                        .filePath(copyofLocation.getRelativePath())
                        .fileName(originalFileName)
                        .fileSize(multipartFile.getSize())
                        .build();

        File savedFile = fileRepository.save(file);

        log.info("savedFile: {}", savedFile);

        return FileUploadResponseDto.fromEntity(savedFile);
    }

    @Override
    @Transactional(readOnly = true)
    public FileDownloadDto downloadFile(Long fileId) {
        File file = fileRepository.findByFileId(fileId)
                                  .orElseThrow(() -> new GlobalException(
                                          ExceptionMessage.FILE_NOT_FOUND));

        // 삭제된 파일이면 다운로드 불가
        if (file.isDeleted()) {
            throw new GlobalException(ExceptionMessage.FILE_ALREADY_DELETED);
        }

        Path base = Paths.get(uploadDir)
                         .toAbsolutePath()
                         .normalize();
        Path target = base.resolve(file.getFilePath())
                          .normalize();

        log.info("base: {}", base);
        log.info("target: {}", target);

        if (!target.startsWith(base)) {
            throw new GlobalException(ExceptionMessage.FILE_PATH_INVALID);
        }

        try {
            UrlResource resource = new UrlResource(target.toUri());

            log.info("resource: {}", resource);

            if (!resource.exists() || !resource.isReadable()) {
                throw new GlobalException(ExceptionMessage.FILE_NOT_READABLE);
            }

            String contentType = Files.probeContentType(target);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            Long length = Files.size(target);

            return new FileDownloadDto(resource, file.getFileName(), length, contentType);

        } catch (IOException error) {
            throw new GlobalException(ExceptionMessage.FILE_INFO_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteFile(Long fileId) {
        File file = fileRepository.findByFileId(fileId)
                                  .orElseThrow(() -> new GlobalException(
                                          ExceptionMessage.FILE_NOT_FOUND));

        file.deleteFile();
    }

    private FilePathInfo generateFilePath(EntityType domain, String newFileName)
            throws IOException {
        // 1. 날짜별 폴더 경로 생성
        LocalDate today = LocalDate.now();
        Path dir = Paths.get(uploadDir, domain.name(), String.valueOf(today.getYear()),
                             String.valueOf(today.getMonthValue()),
                             String.valueOf(today.getDayOfMonth()));

        // 2. 해당 폴더 없으면 생성
        Files.createDirectories(dir);

        Path fullPath = dir.resolve(newFileName);

        // 3. DB 저장 경로
        String relativePath = Paths.get(domain.name(), // uploadDir 제외
                                        String.valueOf(today.getYear()),
                                        String.valueOf(today.getMonthValue()),
                                        String.valueOf(today.getDayOfMonth()), newFileName)
                                   .toString();

        return new FilePathInfo(fullPath, relativePath);
    }
}
