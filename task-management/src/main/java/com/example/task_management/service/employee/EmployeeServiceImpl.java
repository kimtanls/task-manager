package com.example.task_management.service.employee;

import com.example.task_management.dto.TaskDto;
import com.example.task_management.entity.Task;
import com.example.task_management.entity.User;
import com.example.task_management.enums.TaskStatus;
import com.example.task_management.repository.TaskRepository;
import com.example.task_management.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private TaskRepository taskRepository;


    private final JwtUtil jwtUtil;

    @Override
    public List<TaskDto> getTaskByUserId() {
        User user = jwtUtil.getLoggedInUser();
       return taskRepository.findAllByUserId(user.getId())
                    .stream()
                    .sorted(Comparator.comparing(Task::getDueDate).reversed())
                    .map(Task::getTaskDTO)
                    .collect(Collectors.toList());

    }

    @Override
    public TaskDto updateTask(Long id, String status) {
        Optional<Task> optionalTask =  taskRepository.findById(id);
        if(optionalTask.isPresent()){
            Task existingTask = optionalTask.get();
            existingTask.setTaskStatus(mapStringToTaskStatus(status));
            return taskRepository.save(existingTask).getTaskDTO();
        }
        throw new EntityNotFoundException("Task not found");
    }

    private TaskStatus mapStringToTaskStatus(String status){
        return switch (status){
            case "PENDING" -> TaskStatus.PENDING;
            case "INPROGRESS" -> TaskStatus.INPROGRESS;
            case "COMPLETED" -> TaskStatus.COMPLETED;
            case "DEFERRED" -> TaskStatus.DEFERRED;
            default ->  TaskStatus.CANCELLED;
        };
    }
}
