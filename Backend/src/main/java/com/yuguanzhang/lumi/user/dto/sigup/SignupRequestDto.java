package com.yuguanzhang.lumi.user.dto.sigup;

import com.yuguanzhang.lumi.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
@Getter
public class SignupRequestDto {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
             message = "올바른 이메일 형식을 입력하세요.")
    private String email;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
             message = "비밀번호는 8자 이상, 영문/숫자/특수문자를 모두 포함해야 합니다.")
    private String password;

    private String provider;

    @NotNull(message = "개인정보 동의가 필요합니다.")
    private final Boolean isPrivacyAgreement;

    public SignupRequestDto(String email, String name, String password, String provider,
                            Boolean isPrivacyAgreement) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.provider = provider;
        this.isPrivacyAgreement = isPrivacyAgreement;
    }

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                   .email(email)
                   .name(name)
                   .password(passwordEncoder.encode(password))
                   .provider(provider == null ? "local" : provider)
                   .isPrivacyAgreement(isPrivacyAgreement)
                   .isDeleted("N")
                   .build();
    }
}

