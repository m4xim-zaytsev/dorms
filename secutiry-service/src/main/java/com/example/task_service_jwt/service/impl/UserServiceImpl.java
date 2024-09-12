package com.example.task_service_jwt.service.impl;

import com.example.task_service_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;

}
