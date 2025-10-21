package com.yuguanzhang.lumi.material.controller;

import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.common.dto.PageResponseDto;
import com.yuguanzhang.lumi.material.dto.MaterialRequestDto;
import com.yuguanzhang.lumi.material.dto.MaterialResponseDto;
import com.yuguanzhang.lumi.material.dto.MaterialsResponseDto;
import com.yuguanzhang.lumi.material.service.MaterialService;
import com.yuguanzhang.lumi.user.dto.UserDetailsDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping("/{channel_id}/materials")
    public ResponseEntity<BaseResponseDto<MaterialResponseDto>> createMaterial(
            @PathVariable("channel_id") Long channelId,
            @Valid @RequestBody MaterialRequestDto materialRequestDto,
            @AuthenticationPrincipal UserDetailsDto user) {

        MaterialResponseDto materialResponseDto =
                materialService.createMaterial(channelId, user.getUser(), materialRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(BaseResponseDto.of(HttpStatus.CREATED, materialResponseDto));
    }

    @GetMapping("/{channel_id}/materials")
    public ResponseEntity<PageResponseDto<MaterialsResponseDto>> getMaterials(
            @PathVariable("channel_id") Long channelId, Pageable pageable) {
        Page<MaterialsResponseDto> materials = materialService.getMaterials(channelId, pageable);

        return ResponseEntity.ok(PageResponseDto.page(HttpStatus.OK, materials));
    }

    @GetMapping("/{channel_id}/materials/{material_id}")
    public ResponseEntity<BaseResponseDto<MaterialResponseDto>> getMaterial(
            @PathVariable("channel_id") Long channelId,
            @PathVariable("material_id") Long materialId) {

        MaterialResponseDto materialResponseDto = materialService.getMaterial(materialId);

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, materialResponseDto));
    }

    @PutMapping("/{channel_id}/materials/{material_id}")
    public ResponseEntity<BaseResponseDto<MaterialResponseDto>> updateMaterial(
            @PathVariable("channel_id") Long channelId,
            @PathVariable("material_id") Long materialId,
            @AuthenticationPrincipal UserDetailsDto user,
            @Valid @RequestBody MaterialRequestDto materialRequestDto) {

        MaterialResponseDto materialResponseDto =
                materialService.updateMaterial(channelId, user.getUser(), materialId,
                                               materialRequestDto);

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, materialResponseDto));
    }

    @DeleteMapping("/{channel_id}/materials/{material_id}")
    public ResponseEntity<BaseResponseDto<MaterialResponseDto>> deleteMaterial(
            @PathVariable("channel_id") Long channelId,
            @PathVariable("material_id") Long materialId,
            @AuthenticationPrincipal UserDetailsDto user) {

        materialService.deleteMaterial(channelId, user.getUser(), materialId);

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, null));
    }
}
