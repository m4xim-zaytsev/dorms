package com.example.task_service_jwt.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/main")
public class MainPageController {

    @GetMapping("/")
    public String mainPage(){
        return "index";
    }

    @GetMapping("/signin")
    public String signinPage(){
        return "signin";
    }
}
