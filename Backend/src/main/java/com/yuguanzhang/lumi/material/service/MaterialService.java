package com.yuguanzhang.lumi.material.service;

import com.yuguanzhang.lumi.material.dto.MaterialRequestDto;
import com.yuguanzhang.lumi.material.dto.MaterialResponseDto;
import com.yuguanzhang.lumi.material.dto.MaterialsResponseDto;
import com.yuguanzhang.lumi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaterialService {

    MaterialResponseDto createMaterial(Long channelId, User user,
                                       MaterialRequestDto materialRequestDto);

    Page<MaterialsResponseDto> getMaterials(Long channelId, Pageable pageable);

    MaterialResponseDto getMaterial(Long materialId);

    MaterialResponseDto updateMaterial(Long channelId, User user, Long materialId,
                                       MaterialRequestDto dto);

    void deleteMaterial(Long channelId, User user, Long materialId);
}
