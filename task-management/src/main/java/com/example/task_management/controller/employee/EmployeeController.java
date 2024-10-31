package com.example.task_management.controller.employee;

import com.example.task_management.dto.CommentDto;
import com.example.task_management.dto.TaskDto;
import com.example.task_management.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getTaskById(){

        return ResponseEntity.ok(employeeService.getTaskByUserId());
    }

    @GetMapping("/task/{id}/{status}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id,@PathVariable String status){
        TaskDto updateTaskDto = employeeService.updateTask(id,status);
        if (updateTaskDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(updateTaskDto);
    }

    @GetMapping("/task/{id}")
    public  ResponseEntity<TaskDto> getTaskById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getTaskById(id));
    }

    @PostMapping("/task/comment/{taskId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long taskId, @RequestParam String content){
        CommentDto createTaskDto = employeeService.createComment(taskId,content);
        if (createTaskDto == null) return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return  ResponseEntity.status(HttpStatus.CREATED).body(createTaskDto);
    }

    @GetMapping("/comment/{taskId}")
    public ResponseEntity<List<CommentDto>> getCommentByTaskId(@PathVariable Long taskId){
        return ResponseEntity.ok(employeeService.getCommentByTaskDto(taskId));
    }
}
