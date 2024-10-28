package com.example.task_management.entity;

import com.example.task_management.dto.TaskDto;
import com.example.task_management.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Date dueDate;
    private TaskStatus taskStatus;
    private String priority;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //khi user bi xoa thi ban ghi o day cung bi xoa
    @JsonIgnore // Bỏ qua thuộc tính user khi chuyển đổi đối tượng sang JSON,
    private User user;

    public TaskDto getTaskDTO(){
        TaskDto taskDto = new TaskDto();
        taskDto.setId(id);
        taskDto.setTitle(title);
        taskDto.setDescription(description);
        taskDto.setEmployeeId(user.getId());
        taskDto.setEmployeeName(user.getName());
        taskDto.setTaskStatus(taskStatus);
        taskDto.setDueDate(dueDate);
        taskDto.setPriority(priority);
        return taskDto;
    }
}
