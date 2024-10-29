package com.example.task_management.controller.admin;

import com.example.task_management.dto.TaskDto;
import com.example.task_management.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(adminService.getUsers());
    }

    @PostMapping("/task")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto){
        TaskDto createTaskDto = adminService.createTask(taskDto);
        if (createTaskDto == null) return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return  ResponseEntity.status(HttpStatus.CREATED).body(createTaskDto);
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks(){
        return ResponseEntity.ok(adminService.getAllTasks());
    }

}
