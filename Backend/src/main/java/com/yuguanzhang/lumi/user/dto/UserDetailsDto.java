package com.yuguanzhang.lumi.user.dto;

import com.yuguanzhang.lumi.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class UserDetailsDto implements UserDetails {

    private final User user;

    // 사용자가 가진 권한(ROLE)을 반환.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    // 로그인할 때 "사용자 이름"으로 뭘 쓸지를 지정.
    @Override
    public String getUsername() {
        return user.getEmail(); // 여기서는 email 쓰는 게 더 자연스러움
    }

    // 로그인할 때 비밀번호 반환
    @Override
    public String getPassword() {
        return user.getPassword();
    }


    //    계정이 만료되었는지? (false면 로그인 불가)
    //
    //    계정이 잠겼는지? (false면 로그인 불가)
    //
    //    비밀번호가 만료되었는지? (false면 로그인 불가)
    //
    //    계정이 활성화(enabled) 상태인지? (false면 로그인 불가)


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
