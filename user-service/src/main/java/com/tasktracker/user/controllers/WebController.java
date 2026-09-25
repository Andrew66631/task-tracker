package com.tasktracker.user.controllers;

import com.tasktracker.user.dto.AuthResponse;
import com.tasktracker.user.dto.LoginRequest;
import com.tasktracker.user.dto.RegisterRequest;
import com.tasktracker.user.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.tasktracker.user.dto.UserResponse;
import com.tasktracker.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final AuthService authService;

    @Value("${jwt.cookie-name}")
    private String cookieName;

    @Value("${jwt.expiration}")
    private long expiration;

    private final UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute LoginRequest request,
            HttpServletResponse response,
            Model model) {
        try {
            AuthResponse auth = authService.login(request);
            setJwtCookie(response, auth.getToken());   // ← ✅ УСТАНАВЛИВАЕМ COOKIE
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", "Неверный логин или пароль");
            model.addAttribute("loginRequest", request);
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute RegisterRequest request,
            HttpServletResponse response,
            Model model) {
        try {
            AuthResponse auth = authService.register(request);
            setJwtCookie(response, auth.getToken());   // ← ✅ УСТАНАВЛИВАЕМ COOKIE
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", request);
            return "register";
        }
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        // Получаем имя текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            UserResponse user = userService.getUserByUsername(username);
            model.addAttribute("user", user);
        }

        return "profile";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. Очищаем SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        // 2. Удаляем JWT cookie
        Cookie jwtCookie = new Cookie(cookieName, null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        // 3. Удаляем JSESSIONID
        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);

        return "redirect:/login?logout=true";
    }

    /**
     * Устанавливает JWT в HttpOnly cookie.
     */
    private void setJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (expiration / 1000));   // expiration в мс → секунды
        response.addCookie(cookie);
    }
}