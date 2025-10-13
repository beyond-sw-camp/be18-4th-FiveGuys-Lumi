package com.yuguanzhang.lumi.material.service;

import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.channel.repository.ChannelUserRepository;
import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.common.service.RoleAuthorizationService;
import com.yuguanzhang.lumi.file.dto.FileUploadResponseDto;
import com.yuguanzhang.lumi.file.entity.File;
import com.yuguanzhang.lumi.file.entity.FileAssociation;
import com.yuguanzhang.lumi.file.enums.EntityType;
import com.yuguanzhang.lumi.file.repository.FileAssociationRepository;
import com.yuguanzhang.lumi.file.repository.FileRepository;
import com.yuguanzhang.lumi.material.dto.MaterialRequestDto;
import com.yuguanzhang.lumi.material.dto.MaterialResponseDto;
import com.yuguanzhang.lumi.material.dto.MaterialsResponseDto;
import com.yuguanzhang.lumi.material.entity.Material;
import com.yuguanzhang.lumi.material.repository.MaterialRepository;
import com.yuguanzhang.lumi.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final ChannelUserRepository channelUserRepository;
    private final FileRepository fileRepository;
    private final FileAssociationRepository fileAssociationRepository;
    private final RoleAuthorizationService roleAuthorizationService;

    //자료 생성
    @Override
    public MaterialResponseDto createMaterial(Long channelId, User user,
                                              MaterialRequestDto materialRequestDto) {

        ChannelUser channelUser =
                channelUserRepository.findByChannel_ChannelIdAndUser_UserId(channelId,
                                                                            user.getUserId())
                                     .orElseThrow(() -> new GlobalException(
                                             ExceptionMessage.CHANNEL_USER_NOT_FOUND));


        //권한 검증
        roleAuthorizationService.checkTutor(channelId, user.getUserId());

        Material material = Material.builder()
                                    .title(materialRequestDto.getTitle())
                                    .content(materialRequestDto.getContent())
                                    .channelUser(channelUser)
                                    .build();

        Material savedMaterial = materialRepository.save(material);

        List<FileUploadResponseDto> fileUploadResponseDtoList = new ArrayList<>();
        if (materialRequestDto.getFileIds() != null) {
            for (Long fileId : materialRequestDto.getFileIds()) {
                File file = fileRepository.findById(fileId)
                                          .orElseThrow(() -> new GlobalException(
                                                  ExceptionMessage.FILE_NOT_FOUND));

                FileAssociation fileAssociation = FileAssociation.builder()
                                                                 .file(file)
                                                                 .entityId(
                                                                         savedMaterial.getMaterialId())
                                                                 .entityType(EntityType.MATERIAL)
                                                                 .channelUser(channelUser)
                                                                 .build();
                fileAssociationRepository.save(fileAssociation);
                fileUploadResponseDtoList.add(FileUploadResponseDto.fromEntity(file));
            }
        }
        return MaterialResponseDto.fromEntity(savedMaterial, fileUploadResponseDtoList);
    }

    //자료 리스트 조회
    @Override
    public Page<MaterialsResponseDto> getMaterials(Long channelId, Pageable pageable) {

        Page<Material> materials =
                materialRepository.findByChannelUser_Channel_ChannelId(channelId, pageable);

        return materials.map(MaterialsResponseDto::fromEntity);
    }

    //자료 상세 조회
    @Override
    public MaterialResponseDto getMaterial(Long materialId) {
        //자료 객체 가져오기
        Material material = materialRepository.findById(materialId)
                                              .orElseThrow(() -> new GlobalException(
                                                      ExceptionMessage.MATERIAL_NOT_FOUND));
        //자료id랑 자료type으로 등록된 파일들 찾기
        List<FileAssociation> fileAssociations =
                fileAssociationRepository.findByEntityIdAndEntityType(materialId,
                                                                      EntityType.MATERIAL);
        //찾은 파일들을 FileUploadResponseDto로 변환
        List<FileUploadResponseDto> fileUploadResponseDto = fileAssociations.stream()
                                                                            .map(fileAssociation -> FileUploadResponseDto.fromEntity(
                                                                                    fileAssociation.getFile()))
                                                                            .toList();

        return MaterialResponseDto.fromEntity(material, fileUploadResponseDto);

    }

    //자료 수정
    @Override
    public MaterialResponseDto updateMaterial(Long channelId, User user, Long materialId,
                                              MaterialRequestDto materialRequestDto) {

        Material material = materialRepository.findById(materialId)
                                              .orElseThrow(() -> new GlobalException(
                                                      ExceptionMessage.MATERIAL_NOT_FOUND));

        //권한 검증
        roleAuthorizationService.checkTutor(channelId, user.getUserId());

        material.updateTitle(materialRequestDto.getTitle());
        material.updateContent(materialRequestDto.getContent());

        // 기존 파일 연결 삭제
        fileAssociationRepository.deleteByEntityIdAndEntityType(materialId, EntityType.MATERIAL);

        List<FileUploadResponseDto> fileUploadResponseDtoList = new ArrayList<>();
        if (materialRequestDto.getFileIds() != null) {
            for (Long fileId : materialRequestDto.getFileIds()) {
                File file = fileRepository.findById(fileId)
                                          .orElseThrow(() -> new GlobalException(
                                                  ExceptionMessage.FILE_NOT_FOUND));

                FileAssociation association = FileAssociation.builder()
                                                             .file(file)
                                                             .entityId(materialId)
                                                             .entityType(EntityType.MATERIAL)
                                                             .channelUser(material.getChannelUser())
                                                             .build();

                fileAssociationRepository.save(association);
                fileUploadResponseDtoList.add(FileUploadResponseDto.fromEntity(file));
            }
        }

        return MaterialResponseDto.fromEntity(material, fileUploadResponseDtoList);
    }

    @Override
    public void deleteMaterial(Long channelId, User user, Long materialId) {

        Material material = materialRepository.findById(materialId)
                                              .orElseThrow(() -> new GlobalException(
                                                      ExceptionMessage.MATERIAL_NOT_FOUND));

        //권한 검증
        roleAuthorizationService.checkTutor(channelId, user.getUserId());

        //어소시에이션에서 삭제하려는 수업 자료와 관련된 파일 연결 삭제
        fileAssociationRepository.deleteByEntityIdAndEntityType(materialId, EntityType.MATERIAL);

        //수업 자료 삭제
        materialRepository.delete(material);
    }

}
