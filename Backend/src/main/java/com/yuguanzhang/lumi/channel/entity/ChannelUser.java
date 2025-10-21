package com.yuguanzhang.lumi.channel.entity;

import com.yuguanzhang.lumi.common.entity.BaseTimeEntity;
import com.yuguanzhang.lumi.role.entity.Role;
import com.yuguanzhang.lumi.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ChannelUsers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChannelUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_user_id")
    private Long channelUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //@ManyToOne → 다대일 관계를 지정
    //fetch = FetchType.LAZY → 필요할 때만 관련 엔티티 조회, 성능 최적화
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    //글자수 제한이 있어야 할까?
    @Column(name = "data")
    private String data;

    @Column(name = "notification_enabled", nullable = false)
    //boolean vs Boolean
    //지금 boolean notificationEnabled → primitive
    //DB 에서는 nullable 여부를 생각하면 Boolean notificationEnabled로 객체형으로 쓰는 경우도 있음
    //하지만 지금은 default true로 사용하니 문제 없음
    private boolean notificationEnabled = true; //해당 채널 알림 여부 default값 true


    public void updateData(String newData) {
        this.data = newData;
    }

    public void updateNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

}
