package com.yuguanzhang.lumi.channel.service;

import com.yuguanzhang.lumi.channel.dto.ChannelsResponseDto;
import com.yuguanzhang.lumi.channel.dto.ChannelRequestDto;
import com.yuguanzhang.lumi.channel.dto.ChannelResponseDto;
import com.yuguanzhang.lumi.channel.entity.Channel;
import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.channel.repository.ChannelRepository;
import com.yuguanzhang.lumi.channel.repository.ChannelUserRepository;
import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.common.service.RoleAuthorizationService;
import com.yuguanzhang.lumi.role.entity.Role;
import com.yuguanzhang.lumi.role.entity.RoleName;
import com.yuguanzhang.lumi.role.repositiry.RoleRepository;
import com.yuguanzhang.lumi.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;

    private final ChannelUserRepository channelUserRepository;

    private final RoleRepository roleRepository;

    private final RoleAuthorizationService roleAuthorizationService;

    @Override
    @Transactional
    public ChannelResponseDto createChannel(User user, ChannelRequestDto channelRequestDto) {
        //Channel 객체 생성
        Channel channel = channelRequestDto.toEntity(); //dto에 메소드 만들어서 사용
        log.info(channel.toString());
        channelRepository.save(channel);

        log.info(channel.toString());

        //생성한 사람에게 TUTOR 역할 부여하기 위해 TUTOR인 Role 만들기
        Role tutorRole = roleRepository.findByRoleName(RoleName.TUTOR)
                                       .orElseThrow(() -> new GlobalException(
                                               ExceptionMessage.ROLE_NOT_FOUND));

        //ChannelUser 객체 생성
        ChannelUser channelUser = ChannelUser.builder()
                                             .channel(channel)
                                             .user(user)
                                             .notificationEnabled(true)
                                             .role(tutorRole)
                                             .build();

        //db에 저장(insert)
        channelUserRepository.save(channelUser);

        return ChannelResponseDto.fromEntity(channel, user.getUserId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChannelsResponseDto> getChannels(User user, Pageable pageable) {
        //채널 가져오기
        Page<ChannelUser> channels = channelUserRepository.findByUser(user, pageable);

        return channels.map(ChannelsResponseDto::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelResponseDto getChannel(User user, Long channelId) {

        Channel channel = channelRepository.findByChannelIdAndChannelUsers_User(channelId, user)
                                           .orElseThrow(() -> new GlobalException(
                                                   ExceptionMessage.CHANNEL_NOT_FOUND));

        ChannelUser channelUser =
                channelUserRepository.findByChannel_ChannelIdAndUser_UserId(channelId,
                                                                            user.getUserId())
                                     .orElseThrow(() -> new GlobalException(
                                             ExceptionMessage.CHANNEL_USER_NOT_FOUND));

        RoleName rolName = channelUser.getRole()
                                      .getRoleName();

        return ChannelResponseDto.fromEntity(channel, rolName);
    }

    @Override
    @Transactional
    public ChannelResponseDto updateChannel(Long channelId, User user,
                                            ChannelRequestDto channelRequestDto) {
        //권한 체크
        roleAuthorizationService.checkTutor(channelId, user.getUserId());

        //수정할 채널 가져오기
        Channel channel = channelRepository.findById(channelId)
                                           .orElseThrow(() -> new GlobalException(
                                                   ExceptionMessage.CHANNEL_NOT_FOUND));

        //채널 수정
        channel.updateName(channelRequestDto.getName());
        channel.updateSubject(channelRequestDto.getSubject());

        return ChannelResponseDto.fromEntity(channel);
    }

    @Override
    public ChannelResponseDto deleteChannel(Long channelId, User user) {

        //권한 체크
        roleAuthorizationService.checkTutor(channelId, user.getUserId());

        //삭제할 채널 가져오기
        Channel channel = channelRepository.findById(channelId)
                                           .orElseThrow(() -> new GlobalException(
                                                   ExceptionMessage.CHANNEL_NOT_FOUND));

        //채널 삭제
        channel.getChannelUsers()
               .clear();
        channelRepository.delete(channel);

        return ChannelResponseDto.fromEntity(channel);
    }

}
