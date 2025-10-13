package com.yuguanzhang.lumi.user.service.login;

import com.yuguanzhang.lumi.user.dto.login.LoginRequestDto;
import com.yuguanzhang.lumi.user.dto.login.LoginResponseDto;


public interface LoginService {
    LoginResponseDto login(LoginRequestDto request);
}
