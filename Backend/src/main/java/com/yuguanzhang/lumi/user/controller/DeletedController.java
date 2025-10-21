package com.yuguanzhang.lumi.user.controller;

import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.user.dto.deleted.DeletedRequestDto;
import com.yuguanzhang.lumi.user.dto.deleted.DeletedResponseDto;
import com.yuguanzhang.lumi.user.service.deleted.DeletedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeletedController {
    private final DeletedService deletedService;


    @PatchMapping("/api/user/me")
    public BaseResponseDto<DeletedResponseDto> deleted(@RequestBody DeletedRequestDto request) {
        DeletedResponseDto response = deletedService.deleted(request);

        return BaseResponseDto.of(HttpStatus.OK, response);
    }

}
