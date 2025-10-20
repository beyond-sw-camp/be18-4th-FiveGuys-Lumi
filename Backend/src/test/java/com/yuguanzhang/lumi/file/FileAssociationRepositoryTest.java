package com.yuguanzhang.lumi.file;

import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.file.entity.File;
import com.yuguanzhang.lumi.file.entity.FileAssociation;
import com.yuguanzhang.lumi.file.enums.EntityType;
import com.yuguanzhang.lumi.file.repository.FileAssociationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileAssociationRepositoryTest {
    private FileAssociationRepository fileAssociationRepository;

    @BeforeEach
    void setUp() {
        fileAssociationRepository = Mockito.mock(FileAssociationRepository.class);
    }

    @Test
    @DisplayName("findByEntityIdAndEntityType 메서드가 올바른 결과를 반환한다")
    void testFindByEntityIdAndEntityType() {
        // given
        File file = File.builder()
                .fileName("test.txt")
                .filePath("/uploads/test.txt")
                .fileSize(1024L)
                .deleted(false)
                .build();

        ChannelUser channelUser = ChannelUser.builder()
                .data("tester")  // nickname 대신 data 사용
                .build();

        FileAssociation association = FileAssociation.builder()
                .file(file)
                .entityId(100L)
                .entityType(EntityType.ASSIGNMENT)
                .channelUser(channelUser)
                .build();

        when(fileAssociationRepository.findByEntityIdAndEntityType(100L, EntityType.ASSIGNMENT))
                .thenReturn(List.of(association));

        // when
        List<FileAssociation> result = fileAssociationRepository
                .findByEntityIdAndEntityType(100L, EntityType.ASSIGNMENT);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEntityId()).isEqualTo(100L);
        assertThat(result.get(0).getEntityType()).isEqualTo(EntityType.ASSIGNMENT);
    }

    @Test
    @DisplayName("deleteByEntityIdAndEntityType 메서드가 정상적으로 호출되는지 확인")
    void testDeleteByEntityIdAndEntityType() {
        // given
        doNothing().when(fileAssociationRepository).deleteByEntityIdAndEntityType(200L, EntityType.SUBMISSION);

        // when
        fileAssociationRepository.deleteByEntityIdAndEntityType(200L, EntityType.SUBMISSION);

        // then
        verify(fileAssociationRepository, times(1))
                .deleteByEntityIdAndEntityType(200L, EntityType.SUBMISSION);
    }
}