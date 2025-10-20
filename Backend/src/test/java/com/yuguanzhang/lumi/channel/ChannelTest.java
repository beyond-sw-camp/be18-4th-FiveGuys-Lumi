package com.yuguanzhang.lumi.channel.service;

import com.yuguanzhang.lumi.channel.dto.ChannelRequestDto;
import com.yuguanzhang.lumi.channel.dto.ChannelResponseDto;
import com.yuguanzhang.lumi.channel.entity.Channel;
import com.yuguanzhang.lumi.channel.repository.ChannelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

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

    @InjectMocks
    private ChannelServiceImpl channelService; // 실제 서비스 구현체

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        when(channelRepository.save(any(Channel.class))).thenReturn(saved);

        // when
        ChannelResponseDto result = channelService.createChannel(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("테스트채널");
        assertThat(result.getSubject()).isEqualTo("SpringBoot");
        verify(channelRepository, times(1)).save(any(Channel.class));
    }

    @Test
    void 채널_조회_테스트() {
        // given
        Channel channel = Channel.builder()
                .channelId(1L)
                .name("조회채널")
                .subject("JPA")
                .build();

        when(channelRepository.findById(1L)).thenReturn(Optional.of(channel));

        // when
        ChannelResponseDto result = channelService.getChannelById(1L);

        // then
        assertThat(result.getName()).isEqualTo("조회채널");
        assertThat(result.getSubject()).isEqualTo("JPA");
        verify(channelRepository, times(1)).findById(1L);
    }
}
