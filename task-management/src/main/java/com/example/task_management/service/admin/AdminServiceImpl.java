package com.example.task_management.service.admin;

import com.example.task_management.dto.TaskDto;
import com.example.task_management.dto.UserDto;
import com.example.task_management.entity.Task;
import com.example.task_management.entity.User;
import com.example.task_management.enums.TaskStatus;
import com.example.task_management.enums.UserRole;
import com.example.task_management.repository.TaskRepository;
import com.example.task_management.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
public class AdminServiceImpl implements  AdminService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        if (taskDto.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID must not be null");
        }
        Optional<User> optionalUser = userRepository.findById(taskDto.getEmployeeId());
        log.info(String.valueOf(taskDto.getEmployeeId()));
        if (optionalUser.isPresent()){
            Task task = new Task();
            task.setTitle(taskDto.getTitle());
            task.setDescription(taskDto.getDescription());
            task.setPriority(taskDto.getPriority());
            task.setDueDate(taskDto.getDueDate());
            task.setTaskStatus(TaskStatus.INPROGRESS);
            task.setUser(optionalUser.get());
            return taskRepository.save(task).getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDto> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }
}
