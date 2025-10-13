package com.yuguanzhang.lumi.channel.entity;

import com.yuguanzhang.lumi.role.entity.Role;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "Invitations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long invitationId;

    @Column(name = "invitation_code", nullable = false, unique = true, length = 8)
    private String invitationCode;

    //@ManyToOne → 다대일 관계를 지정
    //fetch = FetchType.LAZY → 필요할 때만 관련 엔티티 조회, 성능 최적화
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    //초대 코드 유효 기간
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    //초대 코드 사용 여부
    @Column(nullable = false)
    private boolean used = false;


    //초대 코드 사용하면 값 변경
    public void markUsed() {
        this.used = true;
    }

    //초대 코드 유효기간 검사
    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }
}
