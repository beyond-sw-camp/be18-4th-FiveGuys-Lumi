package com.yuguanzhang.lumi.channel.service;

import com.yuguanzhang.lumi.channel.dto.InvitationRequestDto;
import com.yuguanzhang.lumi.channel.dto.InvitationResponseDto;
import com.yuguanzhang.lumi.channel.entity.Channel;
import com.yuguanzhang.lumi.channel.entity.Invitation;
import com.yuguanzhang.lumi.channel.repository.ChannelRepository;
import com.yuguanzhang.lumi.channel.repository.InvitationRepository;
import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.common.service.RoleAuthorizationService;
import com.yuguanzhang.lumi.role.entity.Role;
import com.yuguanzhang.lumi.role.repositiry.RoleRepository;
import com.yuguanzhang.lumi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final ChannelRepository channelRepository;
    private final RoleRepository roleRepository;
    private final RoleAuthorizationService roleAuthorizationService;

    @Transactional
    public InvitationResponseDto createInvitation(Long channelId, User user,
                                                  InvitationRequestDto invitationRequestDto) {

        //초대를 생성한 채널 가져오기
        Channel channel = channelRepository.findById(channelId)
                                           .orElseThrow(() -> new GlobalException(
                                                   ExceptionMessage.CHANNEL_NOT_FOUND));

        //요청한 사람이 튜터인지 검증
        roleAuthorizationService.checkTutor(channelId, user.getUserId());

        //초대를 생성할 때 정한 역할 가져오기
        Role role = roleRepository.findById(invitationRequestDto.getRoleId())
                                  .orElseThrow(() -> new GlobalException(
                                          ExceptionMessage.ROLE_NOT_FOUND));

        //8자리 랜덤 문자열 (영문+숫자 혼합)
        String code;
        //혹시 모를 중복 코드 검증
        do {
            code = UUID.randomUUID()
                       .toString()
                       .substring(0, 8);
        } while (invitationRepository.existsByInvitationCode(code));

        //초대 객체 생성
        Invitation invitation = Invitation.builder()
                                          .channel(channel)
                                          .role(role)
                                          .invitationCode(code)
                                          .expiredAt(LocalDateTime.now()
                                                                  .plusDays(7))
                                          .build();

        //db에 생성된 초대 저장
        invitationRepository.save(invitation);

        return InvitationResponseDto.fromEntity(invitation);
    }

}
