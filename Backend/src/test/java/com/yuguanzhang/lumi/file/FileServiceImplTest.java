package com.yuguanzhang.lumi.file;

import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.file.dto.FileUploadResponseDto;
import com.yuguanzhang.lumi.file.entity.File;
import com.yuguanzhang.lumi.file.enums.EntityType;
import com.yuguanzhang.lumi.file.repository.FileRepository;
import com.yuguanzhang.lumi.file.service.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FileServiceImplTest {
    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        fileService = new FileServiceImpl(fileRepository, "uploads");
    }

    @Test
    @DisplayName("uploadFile() 정상 동작")
    void testUploadFile() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());

        File savedFile = File.builder().fileId(1L).fileName("test.txt").filePath("relative/test.txt").fileSize(5L).build();
        when(fileRepository.save(any(File.class))).thenReturn(savedFile);

        FileUploadResponseDto result = fileService.uploadFile(EntityType.ASSIGNMENT, multipartFile);

        assertThat(result).isNotNull();
        assertThat(result.getFileId()).isEqualTo(1L);
//        verify(fileRepository, times((int) 1)).save(any(File.class));
    }

    @Test
    @DisplayName("downloadFile() 존재하지 않는 파일 예외")
    void testDownloadFileNotFound() {
        when(fileRepository.findByFileId(1L)).thenReturn(Optional.empty());

        GlobalException exception = assertThrows(GlobalException.class, () -> fileService.downloadFile(1L));

        // ✅ 예외 메시지 문자열 비교
        assertThat(exception.getMessage()).isEqualTo(ExceptionMessage.FILE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("deleteFile() 정상 동작")
    void testDeleteFile() {
        File file = File.builder().fileId(1L).fileName("test.txt").deleted(false).build();
        when(fileRepository.findByFileId(1L)).thenReturn(Optional.of(file));

        fileService.deleteFile(1L);

        assertThat(file.isDeleted()).isTrue();
        assertThat(file.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("deleteFile() 존재하지 않는 파일 예외")
    void testDeleteFileNotFound() {
        when(fileRepository.findByFileId(1L)).thenReturn(Optional.empty());

        GlobalException exception = assertThrows(GlobalException.class, () -> fileService.deleteFile(1L));

        // ✅ 메시지 문자열 비교 (getMessage()는 문자열임)
        assertThat(exception.getMessage()).isEqualTo(ExceptionMessage.FILE_NOT_FOUND.getMessage());

    }
}