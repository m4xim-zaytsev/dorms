package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.entity.dto.ProductDto;
import com.example.task_service_jwt.entity.dto.UserDetailsDto;
import com.example.task_service_jwt.security.AppUserDetails;
import com.example.task_service_jwt.security.SecurityService;
import com.example.task_service_jwt.security.jwt.JwtUtils;
import com.example.task_service_jwt.service.impl.UserServiceImpl;
import com.example.task_service_jwt.web.model.request.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserProfilePageController {

    private final SecurityService securityService;
    private final UserServiceImpl userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String profilePage() {
        return "me";
    }

    @GetMapping("/my")
    public String myProducts(){
        return "my";
    }

    @GetMapping("/my/products")
    public ResponseEntity<List<ProductDto>> getMyProducts(@AuthenticationPrincipal AppUserDetails userDetails) {
        List<ProductDto> products = userService.getProductsByUserId(userDetails.getId());
        return ResponseEntity.ok(products);
    }

    @ResponseBody
    @GetMapping("/details")
    public ResponseEntity<UserDetailsDto> getUserDetails(@AuthenticationPrincipal AppUserDetails userDetails) {
        UserDetailsDto userDetailsDto = new UserDetailsDto(
                userDetails.getUser().getName(),
                userDetails.getEmail(),
                userDetails.getUsername()
        );
        return ResponseEntity.ok(userDetailsDto);
    }

    @ResponseBody
    @PutMapping("/update")
    public ResponseEntity<String> updateUserDetails(@RequestBody UserDetailsDto userDetailsDto,
                                                    @AuthenticationPrincipal AppUserDetails userDetails) {
        if (userDetailsDto == null || userDetailsDto.getUsername() == null || userDetailsDto.getName() == null) {
            throw new IllegalArgumentException("User details cannot be null");
        }
        User userToUpdate = userService.getUserByUsername(userDetails.getUsername());
        UserDetailsDto dataToUserUpdate = userDetailsDto;
        userService.updateUserDetails(userToUpdate, dataToUserUpdate);
        // Генерация нового токена
        String newJwtToken = jwtUtils.generateTokenFromUsername(userDetailsDto.getUsername());

        // Возвращение нового токена клиенту
        return ResponseEntity.ok(newJwtToken);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                                 @AuthenticationPrincipal AppUserDetails userDetails) {
        // Проверка входных данных
        if (changePasswordRequest == null || changePasswordRequest.getNewPassword() == null || changePasswordRequest.getCurrentPassword() == null) {
            return ResponseEntity.badRequest().body("Пароль не может быть пустым");
        }
        // Получаем текущего пользователя из контекста безопасности
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        // Проверяем текущий пароль
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), currentUser.getPassword())) {
            return ResponseEntity.badRequest().body("Текущий пароль неверен");
        }
        // Обновляем пароль пользователя
        currentUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userService.saveUser(currentUser); // Сохранение пользователя

        log.info("Пароль пользователя {} был успешно изменен", currentUser.getUsername());

        return ResponseEntity.ok("Пароль успешно изменен");
    }

    @GetMapping("/hello")
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