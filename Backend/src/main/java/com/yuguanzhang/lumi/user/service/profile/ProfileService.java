package com.yuguanzhang.lumi.user.service.profile;

import com.yuguanzhang.lumi.user.dto.profile.ProfileResoponseDto;

import java.util.UUID;

public interface ProfileService {
    ProfileResoponseDto getProfile(UUID userId);
}
