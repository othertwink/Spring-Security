package org.othertwink.employeeapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GitHubProfileController {

    @GetMapping("/user")
    public String userProfile(@AuthenticationPrincipal OAuth2User principal, Model model) {
        // Получаем информацию из профиля пользователя GitHub
        model.addAttribute("name", principal.getAttribute("name")); // Имя пользователя
        model.addAttribute("login", principal.getAttribute("login")); // Логин на GitHub
        model.addAttribute("id", principal.getAttribute("id")); // ID пользователя на GitHub
        model.addAttribute("email", principal.getAttribute("email")); // Email пользователя

        // Возвращаем шаблон для отображения профиля
        return "user"; // Отображение страницы профиля пользователя
    }

    @GetMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        // Вызываем механизм логаута Spring Security
        request.logout();
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Возвращаем Thymeleaf шаблон login.html
    }

}
