package com.example.task_management.entity;

import com.example.task_management.dto.CommentDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Task task;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "notification_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Notification notification;

    public CommentDto getCommentDto(){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(id);
        commentDto.setContent(content);
        commentDto.setCreatedAt(createdAt);
        commentDto.setTaskId(task.getId());
        commentDto.setPostedBy(user.getName());
        return commentDto;

    }
}
