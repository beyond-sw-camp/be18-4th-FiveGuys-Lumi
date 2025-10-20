package com.yuguanzhang.lumi.file;

import com.yuguanzhang.lumi.file.entity.File;
import com.yuguanzhang.lumi.file.repository.FileRepository;
import com.yuguanzhang.lumi.file.scheduler.FileCleanupScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileCleanupSchedulerTest {
    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileCleanupScheduler scheduler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("삭제 플래그(true) 파일 정리 실행")
    void testCleanupIsDeletedFile() throws IOException {
        // given
        File file1 = File.builder().fileId(1L).filePath("path/to/file1.txt").deleted(true).build();
        File file2 = File.builder().fileId(2L).filePath("path/to/file2.txt").deleted(true).build();

        List<File> deletedFiles = List.of(file1, file2);
        when(fileRepository.findByDeletedTrue()).thenReturn(deletedFiles);

        // Mock Files.deleteIfExists
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.deleteIfExists(Path.of("path/to/file1.txt"))).thenReturn(true);
            filesMock.when(() -> Files.deleteIfExists(Path.of("path/to/file2.txt"))).thenReturn(true);

            // when
            scheduler.cleanupIsDeletedFile();

            // then
            verify(fileRepository, times(1)).findByDeletedTrue();
            verify(fileRepository, times(1)).delete(file1);
            verify(fileRepository, times(1)).delete(file2);

            filesMock.verify(() -> Files.deleteIfExists(Path.of("path/to/file1.txt")));
            filesMock.verify(() -> Files.deleteIfExists(Path.of("path/to/file2.txt")));
        }
    }
}