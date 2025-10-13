package com.yuguanzhang.lumi.common.jwt.service.Refresh;

import com.yuguanzhang.lumi.user.dto.login.LoginResponseDto;
import com.yuguanzhang.lumi.common.jwt.dto.RefreshRequestDto;

public interface RefreshService {
    LoginResponseDto refresh(RefreshRequestDto request);
}
