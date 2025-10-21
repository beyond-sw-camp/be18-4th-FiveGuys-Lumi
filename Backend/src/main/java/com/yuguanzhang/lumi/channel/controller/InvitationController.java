package com.yuguanzhang.lumi.channel.controller;

import com.yuguanzhang.lumi.channel.dto.InvitationRequestDto;
import com.yuguanzhang.lumi.channel.dto.InvitationResponseDto;
import com.yuguanzhang.lumi.channel.service.InvitationService;
import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.user.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    //@Auth어쩌구로 넘기고 user로 서비스에서 userid비교하고 request에서 id 빼기
    @PostMapping("/{channel_id}/invitations")
    public ResponseEntity<BaseResponseDto<InvitationResponseDto>> createInvitation(
            @PathVariable("channel_id") Long channelId,
            @AuthenticationPrincipal UserDetailsDto user,
            @RequestBody InvitationRequestDto invitationRequestDto) {

        InvitationResponseDto invitationResponseDto =
                invitationService.createInvitation(channelId, user.getUser(), invitationRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(BaseResponseDto.of(HttpStatus.CREATED, invitationResponseDto));
    }
}
