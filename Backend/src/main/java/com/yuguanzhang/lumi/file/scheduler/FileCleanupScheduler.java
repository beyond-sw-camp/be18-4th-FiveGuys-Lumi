package com.yuguanzhang.lumi.file.scheduler;

import com.yuguanzhang.lumi.file.entity.File;
import com.yuguanzhang.lumi.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class FileCleanupScheduler {
    private final FileRepository fileRepository;

    // 매일 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupIsDeletedFile() {
        log.info("isDeleted true인 파일들 삭제");

        List<File> fileEntities = fileRepository.findByDeletedTrue();

        log.info("fileEntities: {}", fileEntities);

        for (File file : fileEntities) {
            try {
                // 실제 디스크에서 파일 삭제
                Path path = Paths.get(file.getFilePath());
                Files.deleteIfExists(path);

                // DB 삭제
                fileRepository.delete(file);
            } catch (IOException e) {
                log.error("파일 삭제 실패: {}", file.getFilePath(), e);
            }
        }

    }

}
