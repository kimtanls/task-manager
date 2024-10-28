package com.example.task_management.service.admin;

import com.example.task_management.dto.UserDto;
import com.example.task_management.entity.User;
import com.example.task_management.enums.UserRole;
import com.example.task_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements  AdminService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }
}
