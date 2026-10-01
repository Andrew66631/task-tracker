package com.tasktracker.user.controller;

import com.tasktracker.user.dto.AuthResponse;
import com.tasktracker.user.dto.LoginRequest;
import com.tasktracker.user.dto.RegisterRequest;
import com.tasktracker.user.dto.UserResponse;
import com.tasktracker.user.service.AuthService;
import com.tasktracker.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final AuthService authService;
    private final UserService userService;

    @Value("${jwt.cookie-name}")
    private String cookieName;

    @Value("${jwt.expiration}")
    private long expiration;

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
            setJwtCookie(response, auth.getToken());
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
            setJwtCookie(response, auth.getToken());
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", request);
            return "register";
        }
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            UserResponse user = userService.getUserByUsername(username);
            model.addAttribute("user", user);

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        }

        return "profile";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        Cookie jwtCookie = new Cookie(cookieName, null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);

        return "redirect:/login?logout=true";
    }

    private void setJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (expiration / 1000));
        response.addCookie(cookie);
    }
}