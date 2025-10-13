package com.yuguanzhang.lumi.file.entity;

import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.file.enums.EntityType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

/*
    FileAssociation은 도메인(ASSIGNMENTS, SUBMISSIONS, MATERIALS)와 연결하는 제 삼 매핑 테이블입니다.
    entityId, entityType은 논리적 외래키입니다.
    ex) ASSIGNMENTS의 등록 로직의 경우 등록했을 때의 id 값을 entityId에 매핑하고, entityType은 ASSIGNMENTS로 설정하면 됩니다.
    추후 entityId가 NULL인 경우 스케줄러로 DB 에서 제거할 에정입니다.
 */
@Entity
@Getter
@Table(name = "File_Associations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileAssociation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_association_id")
    private Long fileAssociationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    @Column(name = "entity_id")
    private Long entityId;

    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @ManyToOne
    @JoinColumn(name = "channel_user_id")
    private ChannelUser channelUser;
}
