package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.security.SecurityService;
import com.example.task_service_jwt.web.model.request.CreateUserRequest;
import com.example.task_service_jwt.web.model.request.LoginRequest;
import com.example.task_service_jwt.web.model.response.AuthResponse;
import com.example.task_service_jwt.web.model.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final SecurityService securityService;


    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> registerUser(@RequestBody CreateUserRequest request) {
        securityService.register(request);  // Реализуйте этот метод в SecurityService для регистрации пользователя
        return ResponseEntity.ok(new SimpleResponse("User created"));
    }

    @ResponseBody
    @PostMapping("/login")
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
    @PostMapping("/logout")
    public ResponseEntity<SimpleResponse> logoutUser(@AuthenticationPrincipal UserDetails userDetails) {
        securityService.logout();  // Убедитесь, что этот метод обрабатывает выход пользователя.
        return ResponseEntity.ok(new SimpleResponse("Пользователь вышел, имя пользователя: " + userDetails.getUsername()));
    }
}
