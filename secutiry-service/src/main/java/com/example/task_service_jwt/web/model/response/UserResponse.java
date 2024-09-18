package com.example.task_service_jwt.web.model.response;

import com.example.task_service_jwt.entity.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String name;
    private Set<RoleType> roles = new HashSet<>();

}
