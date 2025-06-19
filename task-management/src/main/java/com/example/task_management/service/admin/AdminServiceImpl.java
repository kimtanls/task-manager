package com.example.task_management.service.admin;

import com.example.task_management.dto.CommentDto;
import com.example.task_management.dto.TaskDto;
import com.example.task_management.dto.UserDto;
import com.example.task_management.entity.Comment;
import com.example.task_management.entity.Notification;
import com.example.task_management.entity.Task;
import com.example.task_management.entity.User;
import com.example.task_management.enums.TaskStatus;
import com.example.task_management.enums.UserRole;
import com.example.task_management.repository.CommentRepository;
import com.example.task_management.repository.NotificationRepository;
import com.example.task_management.repository.TaskRepository;
import com.example.task_management.repository.UserRepository;
import com.example.task_management.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
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

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JwtUtil jwtUtil;

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

        if (optionalUser.isPresent()){
            Task task = new Task();
            task.setTitle(taskDto.getTitle());
            task.setDescription(taskDto.getDescription());
            task.setPriority(taskDto.getPriority());
            task.setDueDate(taskDto.getDueDate());
            task.setTaskStatus(TaskStatus.INPROGRESS);
            task.setUser(optionalUser.get());
            Task saveTask = taskRepository.save(task);

            // Tạo đối tượng Notification
            Notification notification = new Notification();
            notification.setMessage("A new task has been assigned to you: " + task.getTitle());
            notification.setStatus("UNREAD");
            notification.setType("NEW_TASK");
            notification.setTask(saveTask);
            notification.setUser(optionalUser.get());

            notificationRepository.save(notification);

            return saveTask.getTaskDTO();
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

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(Task::getTaskDTO).orElse(null);
    }

    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(taskDto.getEmployeeId());
        if (optionalTask.isPresent()){
            Task existingTask = optionalTask.get();
            existingTask.setTitle(taskDto.getTitle());
            existingTask.setDescription(taskDto.getDescription());
            existingTask.setDueDate(taskDto.getDueDate());
            existingTask.setPriority(taskDto.getPriority());
            existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDto.getTaskStatus())));
            existingTask.setUser(optionalUser.get());
            Task saveTask =  taskRepository.save(existingTask);

            Notification notification = new Notification();
            notification.setMessage("Task \"" + optionalTask.get().getTitle() + "\" has been update " );
            notification.setTask(existingTask);
            notification.setUser(existingTask.getUser());
            notification.setType("TASK_UPDATE");
            notification.setStatus("UNREAD");

            notificationRepository.save(notification);

            return saveTask.getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDto> searchTaskByTitle(String title) {
        return taskRepository.findAllByTitle(title)
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }
    @Override
    public CommentDto createComment(Long taskId, String content) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        User user = jwtUtil.getLoggedInUser();

        if (optionalTask.isPresent() && user != null) {

            Task task = optionalTask.get();
            User employeeUser = task.getUser(); // Lấy employee liên quan đến công việc này

            // Tạo comment mới
            Comment comment = new Comment();
            comment.setCreatedAt(new Date());
            comment.setContent(content);
            comment.setTask(optionalTask.get());
            comment.setUser(user);

            // Lưu comment vào cơ sở dữ liệu trước
            Comment savedComment = commentRepository.save(comment);

            // Tạo thông báo
            Notification notification = new Notification();
            notification.setMessage("Admin comment on task \"" + optionalTask.get().getTitle() + "\": \"" + content + "\"");
            notification.setTask(optionalTask.get()); // Gán task liên quan đến thông báo
            notification.setUser(employeeUser);
            notification.setStatus("UNREAD");


            // Lưu thông báo vào cơ sở dữ liệu
            notificationRepository.save(notification);

            // Cập nhật comment để liên kết với notification (nếu cần)
            savedComment.setNotification(notification);

            commentRepository.save(savedComment); // Cập nhật comment với notification

            return savedComment.getCommentDto(); // Trả về DTO của comment đã lưu
        }
        throw new EntityNotFoundException("User or task not found");
    }

    @Override
    public List<CommentDto> getCommentByTaskDto(Long taskId) {
        return commentRepository.findAllByTaskId(taskId)
                .stream()
                .map(Comment::getCommentDto)
                .collect(Collectors.toList());
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
