package com.yuguanzhang.lumi.channel.dto;

import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChannelUserRequestDto {

    //@Size(max = 255, message = "255자를 넘을 수 없습니다.")
    //예외처리하려면 핸들러 필요
    private String data;

    private boolean notificationEnabled;


    public ChannelUser toEntity() {
        return ChannelUser.builder()
                          .data(this.data)
                          .notificationEnabled(this.notificationEnabled)
                          .build();
    }

}
