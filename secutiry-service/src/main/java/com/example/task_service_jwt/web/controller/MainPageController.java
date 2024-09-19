package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.entity.dto.ProductDto;
import com.example.task_service_jwt.security.AppUserDetails;
import com.example.task_service_jwt.security.SecurityService;
import com.example.task_service_jwt.service.impl.ProductServiceImpl;
import com.example.task_service_jwt.web.model.request.CreateUserRequest;
import com.example.task_service_jwt.web.model.request.LoginRequest;
import com.example.task_service_jwt.web.model.response.AuthResponse;
import com.example.task_service_jwt.web.model.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/main")
public class MainPageController {

    private final SecurityService securityService;
    private final ProductServiceImpl productService;

    @GetMapping()
    public String mainPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String userName = (userDetails != null) ? securityService.getUserByUsername(userDetails.getUsername()).getName() : "";
        model.addAttribute("userName", userName);
        return "index";
    }

    @ResponseBody
    @GetMapping("/get-popular-products")
    public ResponseEntity<List<ProductDto>> getPopularProducts() {
        try {
            List<ProductDto> popularProducts = productService.findAll().stream()
                    .map(ProductDto::fromEntity)
                    .collect(Collectors.toList());

            if (popularProducts.isEmpty()) {
                return ResponseEntity.noContent().build(); // Возвращаем статус 204, если нет продуктов
            }

            return ResponseEntity.ok(popularProducts); // Возвращаем список продуктов и статус 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList()); // Возвращаем пустой список и статус 500 в случае ошибки
        }
    }

}

