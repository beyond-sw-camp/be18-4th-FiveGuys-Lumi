package com.yuguanzhang.lumi.todo.entity;

import com.yuguanzhang.lumi.common.entity.BaseCreatedEntity;
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

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "Todos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo extends BaseCreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    @Builder.Default
    private Boolean status = false;

    @Column(name = "due_date")
    private LocalDate dueDate;


    @Column(name = "description")
    private String description;

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateStatus(Boolean status) {
        this.status = status;
    }

}
