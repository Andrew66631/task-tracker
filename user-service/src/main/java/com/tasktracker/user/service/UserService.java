package com.tasktracker.user.service;

import com.tasktracker.user.dto.UserResponse;
import com.tasktracker.user.entity.enums.EmployeeSubRole;
import com.tasktracker.user.entity.User;
import com.tasktracker.user.entity.enums.UserRole;
import com.tasktracker.user.entity.enums.UserStatus;
import com.tasktracker.user.exception.UserNotFoundException;
import com.tasktracker.user.mapper.UserMapper;
import com.tasktracker.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserMapper.INSTANCE.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return UserMapper.INSTANCE.toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getPendingUsers() {
        return userRepository.findByStatus(UserStatus.PENDING).stream()
                .map(UserMapper.INSTANCE::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse approveUser(UUID id, UUID approvedBy) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setStatus(UserStatus.ACTIVE);
        user.setApprovedAt(LocalDateTime.now());
        user.setApprovedBy(approvedBy);
        return UserMapper.INSTANCE.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse rejectUser(UUID id, UUID rejectedBy) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setStatus(UserStatus.REJECTED);
        user.setApprovedAt(LocalDateTime.now());
        user.setApprovedBy(rejectedBy);
        return UserMapper.INSTANCE.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse blockUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setStatus(UserStatus.BLOCKED);
        return UserMapper.INSTANCE.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse unblockUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setStatus(UserStatus.ACTIVE);
        return UserMapper.INSTANCE.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse changeRole(UUID id, UserRole role, EmployeeSubRole subRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setRole(role);
        user.setSubRole(subRole);
        return UserMapper.INSTANCE.toResponse(userRepository.save(user));
    }
}