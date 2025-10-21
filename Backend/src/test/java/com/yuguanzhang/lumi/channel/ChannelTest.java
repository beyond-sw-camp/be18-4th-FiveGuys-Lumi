package com.yuguanzhang.lumi.channel.service;

import com.yuguanzhang.lumi.channel.dto.ChannelRequestDto;
import com.yuguanzhang.lumi.channel.dto.ChannelResponseDto;
import com.yuguanzhang.lumi.channel.entity.Channel;
import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.channel.repository.ChannelRepository;
import com.yuguanzhang.lumi.channel.repository.ChannelUserRepository;
import com.yuguanzhang.lumi.common.service.RoleAuthorizationService;
import com.yuguanzhang.lumi.role.entity.Role;
import com.yuguanzhang.lumi.role.entity.RoleName;
import com.yuguanzhang.lumi.role.repositiry.RoleRepository;
import com.yuguanzhang.lumi.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ChannelService 단위 테스트
 * DB, Spring Boot 환경 없이 순수 자바 로직만 검증
 */
class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ChannelUserRepository channelUserRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleAuthorizationService roleAuthorizationService;

    @InjectMocks
    private ChannelServiceImpl channelService; // 실제 서비스 구현체

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = User.builder()
                .userId(UUID.randomUUID())   // ✅ long → UUID
                .email("tester@test.com")
                .name("테스터")
                .build();
    }

    @Test
    void 채널_생성_테스트() {
        // given
        ChannelRequestDto dto = ChannelRequestDto.builder()
                .name("테스트채널")
                .subject("SpringBoot")
                .build();

        Channel saved = Channel.builder()
                .channelId(1L)
                .name(dto.getName())
                .subject(dto.getSubject())
                .build();

        Role tutorRole = Role.builder()
                .roleName(RoleName.TUTOR)
                .build();

        when(channelRepository.save(any(Channel.class))).thenReturn(saved);
        when(roleRepository.findByRoleName(RoleName.TUTOR)).thenReturn(Optional.of(tutorRole));
        when(channelUserRepository.save(any(ChannelUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        ChannelResponseDto result = channelService.createChannel(testUser, dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("테스트채널");
        assertThat(result.getSubject()).isEqualTo("SpringBoot");
        verify(channelRepository, times(1)).save(any(Channel.class));
        verify(channelUserRepository, times(1)).save(any(ChannelUser.class));
    }

    @Test
    void 채널_조회_테스트() {
        // given
        Channel channel = Channel.builder()
                .channelId(1L)
                .name("조회채널")
                .subject("JPA")
                .build();

        Role role = Role.builder()
                .roleName(RoleName.TUTOR)
                .build();

        ChannelUser channelUser = ChannelUser.builder()
                .user(testUser)
                .channel(channel)
                .role(role)
                .build();

        when(channelRepository.findByChannelIdAndChannelUsers_User(1L, testUser))
                .thenReturn(Optional.of(channel));
        when(channelUserRepository.findByChannel_ChannelIdAndUser_UserId(1L, testUser.getUserId())) // ✅ 수정됨
                .thenReturn(Optional.of(channelUser));

        // when
        ChannelResponseDto result = channelService.getChannel(testUser, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("조회채널");
        assertThat(result.getSubject()).isEqualTo("JPA");
        verify(channelRepository, times(1)).findByChannelIdAndChannelUsers_User(1L, testUser);
    }
}
