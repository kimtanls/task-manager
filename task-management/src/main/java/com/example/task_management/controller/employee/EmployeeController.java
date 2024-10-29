package com.example.task_management.controller.employee;

import com.example.task_management.dto.TaskDto;
import com.example.task_management.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
