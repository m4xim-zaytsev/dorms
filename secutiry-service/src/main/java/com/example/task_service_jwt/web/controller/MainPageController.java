package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.security.AppUserDetails;
import com.example.task_service_jwt.security.SecurityService;
import com.example.task_service_jwt.web.model.request.CreateUserRequest;
import com.example.task_service_jwt.web.model.request.LoginRequest;
import com.example.task_service_jwt.web.model.response.AuthResponse;
import com.example.task_service_jwt.web.model.response.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/v1")
public class MainPageController {

    private final SecurityService securityService;

    public MainPageController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/main")
    public String mainPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String userName = (userDetails != null) ? securityService.getUserByUsername(userDetails.getUsername()).getName() : "";
        model.addAttribute("userName", userName);
        return "index";  // Возвращает основной шаблон Thymeleaf.
    }


    @GetMapping("/user/hello")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            Map<String, String> response = new HashMap<>();
            response.put("username", ",\s"+securityService.getUserByUsername(userDetails.getUsername()).getName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}

