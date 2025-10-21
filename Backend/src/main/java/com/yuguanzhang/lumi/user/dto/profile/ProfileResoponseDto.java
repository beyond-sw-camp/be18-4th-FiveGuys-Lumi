package com.yuguanzhang.lumi.user.dto.profile;

import com.yuguanzhang.lumi.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProfileResoponseDto {
    private final UUID userId;
    private final String email;
    private final String name;

    public static ProfileResoponseDto profileResoponseDto(User user) {
        return new ProfileResoponseDto(user.getUserId(), user.getEmail(), user.getName());
    }
}
