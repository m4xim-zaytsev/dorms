package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.security.SecurityService;
import com.example.task_service_jwt.web.model.request.CreateUserRequest;
import com.example.task_service_jwt.web.model.request.LoginRequest;
import com.example.task_service_jwt.web.model.response.AuthResponse;
import com.example.task_service_jwt.web.model.response.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        String userName = (userDetails != null) ? securityService.getUserByUsername(userDetails.getUsername()).getName() : "Гость";
        model.addAttribute("userName", userName);
        return "index";  // Возвращает основной шаблон Thymeleaf.
    }

    @PostMapping("/auth/register")
    public ResponseEntity<SimpleResponse> registerUser(@RequestBody CreateUserRequest request) {
        securityService.register(request);  // Реализуйте этот метод в SecurityService для регистрации пользователя
        return ResponseEntity.ok(new SimpleResponse("User created"));
    }

    @ResponseBody
    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = securityService.authenticateUser(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    //    @PostMapping("/refresh-token")
//    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request){
//
//        return ResponseEntity.ok(securityService.refreshToken(request));
//    }

    @ResponseBody
    @PostMapping("/auth/logout")
    public ResponseEntity<SimpleResponse> logoutUser(@AuthenticationPrincipal UserDetails userDetails) {
        securityService.logout();  // Убедитесь, что этот метод обрабатывает выход пользователя.
        return ResponseEntity.ok(new SimpleResponse("Пользователь вышел, имя пользователя: " + userDetails.getUsername()));
    }

    @GetMapping("/user/me")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            Map<String, String> response = new HashMap<>();
            response.put("username", securityService.getUserByUsername(userDetails.getUsername()).getName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}

