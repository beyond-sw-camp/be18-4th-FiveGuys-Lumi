package com.yuguanzhang.lumi.common.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    ID_OR_PASSWORD_EMPTY("아이디와 비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("아이디 또는 비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    DELETED_ACCOUNT("삭제된 계정입니다.", HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    EMAIL_NOT_FOUND("이메일 인증이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_VERIFICATION_FAILED("이메일 인증에 실패했거나 만료되었습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_USED("이미 사용중인 이메일 입니다.", HttpStatus.CONFLICT),
    PRIVACY_AGREEMENT_REQUIRED("개인정보 동의가 필요합니다.", HttpStatus.BAD_REQUEST),


    USER_NOT_FOUND("사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ROOM_NOT_FOUND("채팅방 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ROOM_USER_NOT_FOUND("해당 채팅방에서 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CHAT_NOT_FOUND("채팅 메시지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_CHAT_DELETE("본인이 작성한 메시지만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),

    FILE_NOT_FOUND("해당 파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FILE_NOT_READABLE("파일을 읽을 수 없습니다.", HttpStatus.BAD_REQUEST),
    FILE_PATH_INVALID("허용되지 않은 파일 경로입니다.", HttpStatus.FORBIDDEN),
    FILE_INFO_ERROR("파일 정보 조회를 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_ALREADY_DELETED("이미 삭제된 파일입니다.", HttpStatus.GONE),

    TODO_NOT_FOUND("해당 투두를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_TODO_UPDATE("해당 투두를 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_TODO_DELETE("해당 투두를 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),

    ROLE_NOT_FOUND("해당 역할이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    TUTOR_ROLE_REQUIRED("튜터만 이 작업을 수행할 수 있습니다.", HttpStatus.FORBIDDEN),
    TUTOR_NOT_AVAILABLE("튜터는 이 작업을 수행할 수 없습니다.", HttpStatus.FORBIDDEN),
    STUDENT_ROLE_REQUIRED("학생만 이 작업을 수행할 수 있습니다.", HttpStatus.FORBIDDEN),

    CHANNEL_NOT_FOUND("채널이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    CHANNEL_USER_NOT_FOUND("채널에 속하지 않은 사용자입니다.", HttpStatus.NOT_FOUND),
    CHANNEL_ALREADY_JOINED("이미 채널에 참가한 사용자입니다.", HttpStatus.CONFLICT),

    INVITATION_NOT_FOUND("초대코드가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVITATION_NOT_AVAILABLE("초대가 만료되었거나 이미 사용되었습니다.", HttpStatus.BAD_REQUEST),

    MATERIAL_NOT_FOUND("수업 자료가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    GRADE_NOT_FOUND("성적이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    ASSIGNMENT_NOT_FOUND("과제가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    SUBMISSION_DEADLINE_PASSED("제출 마감 기한이 지나 제출할 수 없습니다.", HttpStatus.BAD_REQUEST),
    SUBMISSION_ALREADY_EXISTS("이미 제출이 완료된 과제입니다.", HttpStatus.BAD_REQUEST),
    SUBMISSION_NOT_FOUND("제출된 제출물이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_SUBMISSION_UPDATE("해당 제출을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_SUBMISSION_DELETE("해당 제출을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    EVALUATION_ALREADY_EXISTS("이미 평가가 완료된 제출입니다.", HttpStatus.BAD_REQUEST),
    EVALUATION_NOT_FOUND("평가가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    EVALUATION_NOT_ALLOWED("평가를 하지 않는 과제에 대해서는 평가를 작성할 수 없습니다.", HttpStatus.BAD_REQUEST),

    COURSE_NOT_FOUND("수업을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_COURSE_DELETE("튜터만 수업을 삭제할 수 있습니다.", HttpStatus.FORBIDDEN);


    private final String message;

    private final HttpStatus status;
}
