package com.yuguanzhang.lumi.material.dto;

import com.yuguanzhang.lumi.file.dto.FileUploadResponseDto;
import com.yuguanzhang.lumi.material.entity.Material;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class MaterialResponseDto {

    private final Long materialId;

    private final String title;

    private final String content;

    private final Long channelUserId;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final List<FileUploadResponseDto> files;

    public static MaterialResponseDto fromEntity(Material material,
                                                 List<FileUploadResponseDto> files) {
        return MaterialResponseDto.builder()
                                  .materialId(material.getMaterialId())
                                  .title(material.getTitle())
                                  .content(material.getContent())
                                  .channelUserId(material.getChannelUser()
                                                         .getChannelUserId())
                                  .createdAt(material.getCreatedAt())
                                  .updatedAt(material.getUpdatedAt())
                                  .files(files)
                                  .build();
    }

}
