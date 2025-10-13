package com.yuguanzhang.lumi.channel.controller;


import com.yuguanzhang.lumi.channel.dto.ChannelsResponseDto;
import com.yuguanzhang.lumi.channel.dto.ChannelRequestDto;
import com.yuguanzhang.lumi.channel.dto.ChannelResponseDto;
import com.yuguanzhang.lumi.channel.service.ChannelService;
import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.common.dto.PageResponseDto;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    //채널 생성
    @PostMapping("/channels")
    public ResponseEntity<BaseResponseDto<ChannelResponseDto>> createChannel(
            @AuthenticationPrincipal UserDetailsDto user,
            @Valid @RequestBody ChannelRequestDto channelRequestDto) {

        ChannelResponseDto channelResponse =
                channelService.createChannel(user.getUser(), channelRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(BaseResponseDto.of(HttpStatus.CREATED, channelResponse));
    }

    //채널 리스트 조회
    //요청 예시 : GET /api/channels?page=0&size=10&sort=createdAt,desc
    @GetMapping("/channels")
    public ResponseEntity<PageResponseDto<ChannelsResponseDto>> getChannels(
            @AuthenticationPrincipal UserDetailsDto user, Pageable pageable) {
        Page<ChannelsResponseDto> channels = channelService.getChannels(user.getUser(), pageable);

        return ResponseEntity.ok(PageResponseDto.page(HttpStatus.OK, channels));

    }

    //채널 상세 조회
    @GetMapping("/channels/{channel_id}")
    public ResponseEntity<BaseResponseDto<ChannelResponseDto>> getChannel(
            @AuthenticationPrincipal UserDetailsDto user,
            @PathVariable("channel_id") Long channelId) {
        ChannelResponseDto channelResponse = channelService.getChannel(user.getUser(), channelId);

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, channelResponse));
    }

    //채널 수정
    @PutMapping("/channels/{channel_id}")
    public ResponseEntity<BaseResponseDto<ChannelResponseDto>> updateChannel(
            @PathVariable("channel_id") Long channelId,
            @AuthenticationPrincipal UserDetailsDto user,
            @Valid @RequestBody ChannelRequestDto channelRequestDto) {

        ChannelResponseDto channelResponse =
                channelService.updateChannel(channelId, user.getUser(), channelRequestDto);

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, channelResponse));
    }

    @DeleteMapping("/channels/{channel_id}")
    public ResponseEntity<BaseResponseDto<ChannelResponseDto>> deleteChannel(
            @PathVariable("channel_id") Long channelId,
            @AuthenticationPrincipal UserDetailsDto user) {
        ChannelResponseDto channelResponse =
                channelService.deleteChannel(channelId, user.getUser());

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, channelResponse));
    }

}
