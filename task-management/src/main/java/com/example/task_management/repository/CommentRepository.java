package com.example.task_management.repository;

import com.example.task_management.dto.CommentDto;
import com.example.task_management.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByTaskId(Long taskId);
}
