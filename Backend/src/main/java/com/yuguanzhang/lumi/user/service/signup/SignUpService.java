package com.yuguanzhang.lumi.user.service.signup;

import com.yuguanzhang.lumi.user.dto.sigup.SignupRequestDto;
import com.yuguanzhang.lumi.user.dto.sigup.SignupResponseDto;
import com.yuguanzhang.lumi.user.entity.User;

public interface SignUpService {
    SignupResponseDto processSignup(SignupRequestDto signupRequestDto);

    User findByEmail(String email);
}
