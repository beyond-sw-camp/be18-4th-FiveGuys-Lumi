package com.yuguanzhang.lumi.file;

import com.yuguanzhang.lumi.file.entity.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FileTest {
    @Test
    @DisplayName("Builder로 File 엔티티 생성이 정상적으로 이루어진다.")
    void testFileBuilder() {
        // given
        String fileName = "test.txt";
        String filePath = "/uploads/test.txt";
        Long fileSize = 1024L;

        // when
        File file = File.builder()
                .fileName(fileName)
                .filePath(filePath)
                .fileSize(fileSize)
                .deleted(false)
                .build();

        // then
        assertThat(file).isNotNull();
        assertThat(file.getFileName()).isEqualTo(fileName);
        assertThat(file.getFilePath()).isEqualTo(filePath);
        assertThat(file.getFileSize()).isEqualTo(fileSize);
        assertThat(file.isDeleted()).isFalse();
        assertThat(file.getDeletedAt()).isNull();
    }

    @Test
    @DisplayName("deleteFile() 호출 시 파일이 삭제 상태로 변경된다.")
    void testDeleteFile() {
        // given
        File file = File.builder()
                .fileName("test2.txt")
                .filePath("/uploads/test2.txt")
                .fileSize(2048L)
                .deleted(false)
                .build();

        // when
        file.deleteFile();

        // then
        assertThat(file.isDeleted()).isTrue();
        assertThat(file.getDeletedAt()).isNotNull();
        assertThat(file.getDeletedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}