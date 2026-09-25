package com.tasktracker.user.controllers;

import com.tasktracker.user.dto.ChangeRoleRequest;
import com.tasktracker.user.dto.UserResponse;
import com.tasktracker.user.entity.enums.EmployeeSubRole;
import com.tasktracker.user.entity.enums.UserRole;
import com.tasktracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    /**
     * Страница админ-панели со списком pending-пользователей.
     */
    @GetMapping("/users")
    public String usersPage(Model model) {
        List<UserResponse> pendingUsers = userService.getPendingUsers();
        List<UserResponse> allUsers = userService.getAllUsers();

        model.addAttribute("pendingUsers", pendingUsers);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("subRoles", EmployeeSubRole.values());

        return "admin/users";
    }

    /**
     * Одобрить пользователя (с назначением роли).
     */
    @PostMapping("/users/{id}/approve")
    public String approveUser(
            @PathVariable UUID id,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) EmployeeSubRole subRole) {

        if (role != null) {
            userService.changeRole(id, role, subRole);
        }
        userService.approveUser(id, null);
        return "redirect:/admin/users";
    }

    /**
     * Отклонить пользователя.
     */
    @PostMapping("/users/{id}/reject")
    public String rejectUser(@PathVariable UUID id) {
        userService.rejectUser(id, null);
        return "redirect:/admin/users";
    }

    /**
     * Заблокировать пользователя.
     */
    @PostMapping("/users/{id}/block")
    public String blockUser(@PathVariable UUID id) {
        userService.blockUser(id);
        return "redirect:/admin/users";
    }

    /**
     * Разблокировать пользователя.
     */
    @PostMapping("/users/{id}/unblock")
    public String unblockUser(@PathVariable UUID id) {
        userService.unblockUser(id);
        return "redirect:/admin/users";
    }

    /**
     * Изменить роль пользователя.
     */
    @PostMapping("/users/{id}/role")
    public String changeRole(
            @PathVariable UUID id,
            @RequestParam UserRole role,
            @RequestParam(required = false) EmployeeSubRole subRole) {
        userService.changeRole(id, role, subRole);
        return "redirect:/admin/users";
    }
}