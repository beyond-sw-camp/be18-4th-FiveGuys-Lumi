package com.yuguanzhang.lumi.grade.service;

import com.yuguanzhang.lumi.grade.dto.GradeCategoryGroupDto;
import com.yuguanzhang.lumi.grade.dto.GradeRequestDto;
import com.yuguanzhang.lumi.grade.dto.GradeResponseDto;
import com.yuguanzhang.lumi.user.entity.User;

import java.util.List;

public interface GradeService {

    // 성적 생성
    GradeResponseDto createGrade(Long channelId, GradeRequestDto gradeRequestDto, User user);

    // 성적 리스트 조회
    List<GradeCategoryGroupDto> getGradesGroupedByCategory(Long channelId, User user);

    // 성적 수정
    GradeResponseDto updateGrade(Long channelId, Long gradeId, GradeRequestDto gradeRequestDto,
                                 User user);

    // 성적 삭제
    void deleteGrade(Long channelId, Long gradeId, User user);

}
