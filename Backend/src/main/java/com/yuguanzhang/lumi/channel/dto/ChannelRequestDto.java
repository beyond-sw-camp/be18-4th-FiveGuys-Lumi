package com.yuguanzhang.lumi.channel.dto;

import com.yuguanzhang.lumi.channel.entity.Channel;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChannelRequestDto {

    @NotBlank
    private String name;

    private String subject;


    //받은 정보를 엔티티로 만들어 반환하는 메소드
    public Channel toEntity() {
        return Channel.builder()
                      .name(this.name)
                      .subject(this.subject)
                      .build();
    }

}
