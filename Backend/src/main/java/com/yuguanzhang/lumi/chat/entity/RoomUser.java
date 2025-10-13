package com.yuguanzhang.lumi.chat.entity;

import com.yuguanzhang.lumi.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "RoomUsers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomUser {
    @EmbeddedId
    private RoomUserId roomUserId;

    @MapsId("roomId") // RoomUserId.roomId 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @MapsId("userId") // RoomUserId.userId 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "has_unread")
    private Boolean hasUnread = false;

    @Column(name = "last_message_content")
    private String lastMessageContent;

    @Column(name = "last_message_time")
    private LocalDateTime lastMessageTime;

    public boolean isHasUnread() {
        return Boolean.TRUE.equals(this.hasUnread);
    }

    public void makeUnread() {
        this.hasUnread = true;
    }

    public void resetUnread() {
        this.hasUnread = false;
    }

    public void updateLastMessage(String content, LocalDateTime time) {
        this.lastMessageContent = content;
        this.lastMessageTime = time;
    }
}
