package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.entity.dto.UserDetailsDto;
import com.example.task_service_jwt.security.AppUserDetails;
import com.example.task_service_jwt.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class UserProfilePageController {

    private final SecurityService securityService;

    @GetMapping
    public String profilePage() {
        return "me";
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
}