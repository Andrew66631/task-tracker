package com.tasktracker.user.controllers;

import com.tasktracker.user.dto.CreateProjectForm;
import com.tasktracker.user.dto.ProjectResponse;
import com.tasktracker.user.dto.UserResponse;
import com.tasktracker.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectDashboardController {

    private final RestClient projectRestClient;
    private final UserService userService;

    @Value("${jwt.cookie-name}")
    private String cookieName;

    // ===== Список =====
    @GetMapping
    public String list(HttpServletRequest request, Model model) {
        String jwt = extractJwt(request);

        List<ProjectResponse> projects = projectRestClient.get()
                .uri("/api/projects")
                .header("Cookie", cookieName + "=" + jwt)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProjectResponse>>() {});

        model.addAttribute("projects", projects);
        return "projects/list";
    }

    // ===== Просмотр =====
    @GetMapping("/{id}")
    public String view(@PathVariable UUID id,
                       HttpServletRequest request,
                       Model model) {
        String jwt = extractJwt(request);

        ProjectResponse project = projectRestClient.get()
                .uri("/api/projects/{id}", id)
                .header("Cookie", cookieName + "=" + jwt)
                .retrieve()
                .body(ProjectResponse.class);

        model.addAttribute("project", project);
        return "projects/view";
    }

    // ===== Создание =====
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("project", new CreateProjectForm());
        model.addAttribute("mode", "create");
        return "projects/form";
    }

    @PostMapping
    public String create(@ModelAttribute CreateProjectForm form,
                         HttpServletRequest request,
                         Authentication authentication,
                         Model model) {
        String jwt = extractJwt(request);
        form.setOwnerId(resolveUserId(authentication));

        try {
            projectRestClient.post()
                    .uri("/api/projects")
                    .header("Cookie", cookieName + "=" + jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(form)
                    .retrieve()
                    .body(ProjectResponse.class);
            return "redirect:/projects";
        } catch (RestClientResponseException e) {
            log.error("project-service вернул {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            model.addAttribute("error", "Не удалось создать проект: " + e.getStatusCode());
            model.addAttribute("project", form);
            model.addAttribute("mode", "create");
            return "projects/form";
        }
    }

    // ===== Редактирование =====
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable UUID id,
                           HttpServletRequest request,
                           Model model) {
        String jwt = extractJwt(request);

        ProjectResponse project = projectRestClient.get()
                .uri("/api/projects/{id}", id)
                .header("Cookie", cookieName + "=" + jwt)
                .retrieve()
                .body(ProjectResponse.class);

        CreateProjectForm form = new CreateProjectForm();
        form.setName(project.getName());
        form.setDescription(project.getDescription());
        form.setOwnerId(project.getOwnerId());

        model.addAttribute("project", form);
        model.addAttribute("projectId", id);
        model.addAttribute("mode", "edit");
        return "projects/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable UUID id,
                         @ModelAttribute CreateProjectForm form,
                         HttpServletRequest request,
                         Model model) {
        String jwt = extractJwt(request);

        try {
            projectRestClient.put()
                    .uri("/api/projects/{id}", id)
                    .header("Cookie", cookieName + "=" + jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(form)
                    .retrieve()
                    .body(ProjectResponse.class);
            return "redirect:/projects/" + id;
        } catch (RestClientResponseException e) {
            log.error("project-service вернул {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            model.addAttribute("error", "Не удалось сохранить: " + e.getStatusCode());
            model.addAttribute("project", form);
            model.addAttribute("projectId", id);
            model.addAttribute("mode", "edit");
            return "projects/form";
        }
    }

    // ===== Архивирование =====
    @PostMapping("/{id}/archive")
    public String archive(@PathVariable UUID id, HttpServletRequest request) {
        String jwt = extractJwt(request);

        projectRestClient.put()
                .uri("/api/projects/{id}/archive", id)
                .header("Cookie", cookieName + "=" + jwt)
                .retrieve()
                .toBodilessEntity();

        return "redirect:/projects/" + id;
    }

    // ===== Удаление =====
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id, HttpServletRequest request) {
        String jwt = extractJwt(request);

        projectRestClient.delete()
                .uri("/api/projects/{id}", id)
                .header("Cookie", cookieName + "=" + jwt)
                .retrieve()
                .toBodilessEntity();

        return "redirect:/projects";
    }

    // ===== Helpers =====
    private String extractJwt(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new IllegalStateException("No cookies");
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("JWT cookie missing"));
    }

    private UUID resolveUserId(Authentication authentication) {
        UserResponse user = userService.getUserByUsername(authentication.getName());
        return user.getId();
    }
}