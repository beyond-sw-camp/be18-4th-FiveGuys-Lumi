package com.yuguanzhang.lumi.user.service.logout;

import com.yuguanzhang.lumi.common.jwt.dto.RefreshRequestDto;
import com.yuguanzhang.lumi.user.dto.logout.LogoutResponseDto;

public interface LogoutService {
    LogoutResponseDto logout(RefreshRequestDto request);
}
