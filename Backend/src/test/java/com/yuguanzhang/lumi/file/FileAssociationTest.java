package com.yuguanzhang.lumi.file;

import com.yuguanzhang.lumi.file.entity.File;
import com.yuguanzhang.lumi.file.entity.FileAssociation;
import com.yuguanzhang.lumi.file.enums.EntityType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FileAssociationTest {
    @Test
    void testBuilderAndFields() {
        File file = File.builder().fileName("file.txt").build();
        FileAssociation association = FileAssociation.builder()
                .file(file)
                .entityId(1L)
                .entityType(EntityType.ASSIGNMENT)
                .build();

        assertThat(association.getFile()).isEqualTo(file);
        assertThat(association.getEntityId()).isEqualTo(1L);
        assertThat(association.getEntityType()).isEqualTo(EntityType.ASSIGNMENT);
    }
}