package com.yuguanzhang.lumi.user.service.signup;

import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.email.repository.EmailVerificationRepository;
import com.yuguanzhang.lumi.email.service.EmailVerificationService;
import com.yuguanzhang.lumi.user.dto.sigup.SignupRequestDto;
import com.yuguanzhang.lumi.user.dto.sigup.SignupResponseDto;
import com.yuguanzhang.lumi.user.entity.User;
import com.yuguanzhang.lumi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Override
    @Transactional
    public SignupResponseDto processSignup(SignupRequestDto signupRequestDto) {
        if (signupRequestDto.getIsPrivacyAgreement() == null || !signupRequestDto.getIsPrivacyAgreement()) {
            throw new GlobalException(ExceptionMessage.PRIVACY_AGREEMENT_REQUIRED);
        }

        if (!emailVerificationService.isEmailVerified(signupRequestDto.getEmail())) {
            throw new GlobalException(ExceptionMessage.EMAIL_NOT_FOUND);
        }

        if (userRepository.findByEmail(signupRequestDto.getEmail())
                          .isPresent()) {
            throw new GlobalException(ExceptionMessage.EMAIL_ALREADY_USED);
        }

        User user = signupRequestDto.toEntity(passwordEncoder);
        user.markAsVerified();
        User savedUser = userRepository.save(user);

        emailVerificationRepository.findByEmail(signupRequestDto.getEmail())
                                   .ifPresent(
                                           verification -> verification.associateUser(savedUser));

        return new SignupResponseDto("회원가입 성공", savedUser.getEmail(), savedUser.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                             .orElse(null);
    }
}
