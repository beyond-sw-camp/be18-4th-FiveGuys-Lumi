package com.yuguanzhang.lumi.assignment.service;

import com.yuguanzhang.lumi.assignment.dto.SubmissionRequestDto;
import com.yuguanzhang.lumi.assignment.dto.SubmissionResponseDto;
import com.yuguanzhang.lumi.assignment.entity.Assignment;
import com.yuguanzhang.lumi.assignment.entity.Submission;
import com.yuguanzhang.lumi.assignment.repository.AssignmentRepository;
import com.yuguanzhang.lumi.assignment.repository.SubmissionRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final ChannelUserRepository channelUserRepository;
    private final FileRepository fileRepository;
    private final FileAssociationRepository fileAssociationRepository;
    private final RoleAuthorizationService roleAuthorizationService;

    @Override
    @Transactional
    public SubmissionResponseDto createSubmission(Long channelId, Long assignmentId, User user,
                                                  SubmissionRequestDto submissionRequestDto) {
        ChannelUser channelUser =
                channelUserRepository.findByChannel_ChannelIdAndUser_UserId(channelId,
                                                                            user.getUserId())
                                     .orElseThrow(() -> new GlobalException(
                                             ExceptionMessage.CHANNEL_USER_NOT_FOUND));


        Assignment assignment = assignmentRepository.findById(assignmentId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.ASSIGNMENT_NOT_FOUND));

        //권한 검증
        roleAuthorizationService.checkStudent(channelId, user.getUserId());

        // 마감일 검증 (지났으면 제출 불가)
        if (assignment.getDeadlineAt()
                      .isBefore(LocalDateTime.now())) {
            throw new GlobalException(ExceptionMessage.SUBMISSION_DEADLINE_PASSED);
        }

        //이미 제출이 존재하면 에러
        if (submissionRepository.existsByAssignment_AssignmentIdAndChannelUser_ChannelUserId(
                assignmentId, channelUser.getChannelUserId())) {
            throw new GlobalException(ExceptionMessage.SUBMISSION_ALREADY_EXISTS);
        }

        Submission submission = Submission.builder()
                                          .title(submissionRequestDto.getTitle())
                                          .description(submissionRequestDto.getDescription())
                                          .assignment(assignment)
                                          .channelUser(channelUser)
                                          .build();

        assignment.updateIsSubmission(true);
        Submission saved = submissionRepository.save(submission);

        List<FileUploadResponseDto> fileUploadResponseDtoList = new ArrayList<>();
        if (submissionRequestDto.getFileIds() != null) {
            for (Long fileId : submissionRequestDto.getFileIds()) {
                File file = fileRepository.findById(fileId)
                                          .orElseThrow(() -> new GlobalException(
                                                  ExceptionMessage.FILE_NOT_FOUND));

                FileAssociation association = FileAssociation.builder()
                                                             .file(file)
                                                             .entityId(saved.getSubmissionId())
                                                             .entityType(EntityType.SUBMISSION)
                                                             .channelUser(channelUser)
                                                             .build();

                fileAssociationRepository.save(association);
                fileUploadResponseDtoList.add(FileUploadResponseDto.fromEntity(file));
            }
        }

        return SubmissionResponseDto.fromEntity(saved, fileUploadResponseDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public SubmissionResponseDto getSubmission(Long channelId, Long assignmentId, User user) {

        Submission submission = submissionRepository.findByAssignment_AssignmentId(assignmentId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.SUBMISSION_NOT_FOUND));

        //id랑 type 으로 등록된 파일들 찾기
        List<FileAssociation> fileAssociations =
                fileAssociationRepository.findByEntityIdAndEntityType(submission.getSubmissionId(),
                                                                      EntityType.SUBMISSION);

        //찾은 파일들을 FileUploadResponseDto로 변환
        List<FileUploadResponseDto> fileUploadResponseDto = fileAssociations.stream()
                                                                            .map(fileAssociation -> FileUploadResponseDto.fromEntity(
                                                                                    fileAssociation.getFile()))
                                                                            .toList();

        return SubmissionResponseDto.fromEntity(submission, fileUploadResponseDto);
    }

    @Override
    @Transactional
    public SubmissionResponseDto updateSubmission(Long channelId, Long assignmentId,
                                                  Long submissionId, User user,
                                                  SubmissionRequestDto submissionRequestDto) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.ASSIGNMENT_NOT_FOUND));

        Submission submission = submissionRepository.findById(submissionId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.SUBMISSION_NOT_FOUND));
        //권한 검증
        roleAuthorizationService.checkStudent(channelId, user.getUserId());

        // 본인이 생성한 제출이 맞는지 검증
        if (!submission.getChannelUser()
                       .getUser()
                       .getUserId()
                       .equals(user.getUserId())) {
            throw new GlobalException(ExceptionMessage.UNAUTHORIZED_SUBMISSION_UPDATE);
        }

        // 마감일 검증 (지났으면 제출 불가)
        if (assignment.getDeadlineAt()
                      .isBefore(LocalDateTime.now())) {
            throw new GlobalException(ExceptionMessage.SUBMISSION_DEADLINE_PASSED);
        }

        submission.updateTitle(submissionRequestDto.getTitle());
        submission.updateDescription(submissionRequestDto.getDescription());

        fileAssociationRepository.deleteByEntityIdAndEntityType(submissionId,
                                                                EntityType.SUBMISSION);

        List<FileUploadResponseDto> fileUploadResponseDtoList = new ArrayList<>();
        if (submissionRequestDto.getFileIds() != null) {
            for (Long fileId : submissionRequestDto.getFileIds()) {
                File file = fileRepository.findById(fileId)
                                          .orElseThrow(() -> new GlobalException(
                                                  ExceptionMessage.FILE_NOT_FOUND));

                FileAssociation association = FileAssociation.builder()
                                                             .file(file)
                                                             .entityId(submission.getSubmissionId())
                                                             .entityType(EntityType.SUBMISSION)
                                                             .channelUser(
                                                                     submission.getChannelUser())
                                                             .build();

                fileAssociationRepository.save(association);
                fileUploadResponseDtoList.add(FileUploadResponseDto.fromEntity(file));
            }
        }

        return SubmissionResponseDto.fromEntity(submission, fileUploadResponseDtoList);
    }

    @Override
    @Transactional
    public void deleteSubmission(Long channelId, Long assignmentId, Long submissionId, User user) {


        Submission submission = submissionRepository.findById(submissionId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.SUBMISSION_NOT_FOUND));
        Assignment assignment = assignmentRepository.findById(assignmentId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.ASSIGNMENT_NOT_FOUND));

        // 권한 검증
        roleAuthorizationService.checkStudent(channelId, user.getUserId());

        // 본인이 생성한 제출이 맞는지 검증
        if (!submission.getChannelUser()
                       .getUser()
                       .getUserId()
                       .equals(user.getUserId())) {
            throw new GlobalException(ExceptionMessage.UNAUTHORIZED_SUBMISSION_DELETE);
        }

        fileAssociationRepository.deleteByEntityIdAndEntityType(submissionId,
                                                                EntityType.SUBMISSION);

        assignment.updateIsSubmission(false);
        log.info("제출 : {}", submission.getTitle());
        submissionRepository.delete(submission);

    }
}
