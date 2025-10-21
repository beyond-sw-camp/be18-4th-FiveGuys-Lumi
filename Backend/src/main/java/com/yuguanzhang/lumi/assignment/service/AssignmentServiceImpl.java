package com.yuguanzhang.lumi.assignment.service;

import com.yuguanzhang.lumi.assignment.dto.AssignmentRequestDto;
import com.yuguanzhang.lumi.assignment.dto.AssignmentResponseDto;
import com.yuguanzhang.lumi.assignment.dto.AssignmentsResponseDto;
import com.yuguanzhang.lumi.assignment.entity.Assignment;
import com.yuguanzhang.lumi.assignment.repository.AssignmentRepository;
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
import com.yuguanzhang.lumi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final ChannelUserRepository channelUserRepository;
    private final FileRepository fileRepository;
    private final FileAssociationRepository fileAssociationRepository;
    private final RoleAuthorizationService roleAuthorizationService;

    @Override
    public AssignmentResponseDto createAssignment(Long channelId, User user,
                                                  AssignmentRequestDto assignmentRequestDto) {
        ChannelUser channelUser =
                channelUserRepository.findByChannel_ChannelIdAndUser_UserId(channelId,
                                                                            user.getUserId())
                                     .orElseThrow(() -> new GlobalException(
                                             ExceptionMessage.CHANNEL_USER_NOT_FOUND));

        roleAuthorizationService.checkTutor(channelId, user.getUserId());

        Assignment assignment = Assignment.builder()
                                          .title(assignmentRequestDto.getTitle())
                                          .content(assignmentRequestDto.getContent())
                                          .deadlineAt(assignmentRequestDto.getDeadlineAt())
                                          .isEvaluation(assignmentRequestDto.isEvaluation())
                                          .evaluationDeadlineAt(assignmentRequestDto.getDeadlineAt()
                                                                                    .plusDays(
                                                                                            7)) // ✅ 제출마감 + 7일
                                          .channelUser(channelUser)
                                          .build();

        Assignment savedAssignment = assignmentRepository.save(assignment);

        List<FileUploadResponseDto> fileUploadResponseDtoList = new ArrayList<>();
        if (assignmentRequestDto.getFileIds() != null) {
            for (Long fileId : assignmentRequestDto.getFileIds()) {
                File file = fileRepository.findById(fileId)
                                          .orElseThrow(() -> new GlobalException(
                                                  ExceptionMessage.FILE_NOT_FOUND));

                FileAssociation association = FileAssociation.builder()
                                                             .file(file)
                                                             .entityId(
                                                                     savedAssignment.getAssignmentId())
                                                             .entityType(EntityType.ASSIGNMENT)
                                                             .channelUser(channelUser)
                                                             .build();

                fileAssociationRepository.save(association);
                fileUploadResponseDtoList.add(FileUploadResponseDto.fromEntity(file));
            }
        }

        return AssignmentResponseDto.fromEntity(savedAssignment, fileUploadResponseDtoList);
    }

    @Override
    public Page<AssignmentsResponseDto> getAssignments(Long channelId, Pageable pageable) {
        Page<Assignment> assignments =
                assignmentRepository.findByChannelUser_Channel_ChannelId(channelId, pageable);

        return assignments.map(AssignmentsResponseDto::fromEntity);
    }

    @Override
    public AssignmentResponseDto getAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.ASSIGNMENT_NOT_FOUND));
        //id랑 type 으로 등록된 파일들 찾기
        List<FileAssociation> fileAssociations =
                fileAssociationRepository.findByEntityIdAndEntityType(assignmentId,
                                                                      EntityType.ASSIGNMENT);
        //찾은 파일들을 FileUploadResponseDto로 변환
        List<FileUploadResponseDto> fileUploadResponseDto = fileAssociations.stream()
                                                                            .map(fileAssociation -> FileUploadResponseDto.fromEntity(
                                                                                    fileAssociation.getFile()))
                                                                            .toList();

        return AssignmentResponseDto.fromEntity(assignment, fileUploadResponseDto);
    }


    @Override
    public AssignmentResponseDto updateAssignment(Long channelId, User user, Long assignmentId,
                                                  AssignmentRequestDto assignmentRequestDto) {
        ChannelUser channelUser =
                channelUserRepository.findByChannel_ChannelIdAndUser_UserId(channelId,
                                                                            user.getUserId())
                                     .orElseThrow(() -> new GlobalException(
                                             ExceptionMessage.CHANNEL_USER_NOT_FOUND));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.ASSIGNMENT_NOT_FOUND));

        //권한 검증
        roleAuthorizationService.checkTutor(channelId, user.getUserId());

        assignment.updateAssignment(assignmentRequestDto);

        fileAssociationRepository.deleteByEntityIdAndEntityType(assignmentId,
                                                                EntityType.ASSIGNMENT);

        List<FileUploadResponseDto> fileUploadResponseDtoList = new ArrayList<>();
        if (assignmentRequestDto.getFileIds() != null) {
            for (Long fileId : assignmentRequestDto.getFileIds()) {
                File file = fileRepository.findById(fileId)
                                          .orElseThrow(() -> new GlobalException(
                                                  ExceptionMessage.FILE_NOT_FOUND));

                FileAssociation association = FileAssociation.builder()
                                                             .file(file)
                                                             .entityId(assignmentId)
                                                             .entityType(EntityType.ASSIGNMENT)
                                                             .channelUser(channelUser)
                                                             .build();

                fileAssociationRepository.save(association);
                fileUploadResponseDtoList.add(FileUploadResponseDto.fromEntity(file));
            }
        }

        return AssignmentResponseDto.fromEntity(assignment, fileUploadResponseDtoList);
    }

    @Override
    public void deleteAssignment(Long channelId, User user, Long assignmentId) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.ASSIGNMENT_NOT_FOUND));

        roleAuthorizationService.checkTutor(channelId, user.getUserId());

        fileAssociationRepository.deleteByEntityIdAndEntityType(assignmentId,
                                                                EntityType.ASSIGNMENT);

        assignmentRepository.delete(assignment);

    }

}
