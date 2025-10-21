package com.yuguanzhang.lumi.common.jwt.service.jwt;

public interface JwtService {
    String generateAccessToken(String username);

    String generateRefreshToken(String username);

    String getUsername(String token);

    boolean validateAccessToken(String token);

    boolean validateRefreshToken(String token);

}
