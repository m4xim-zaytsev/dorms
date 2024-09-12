package com.example.task_service_jwt.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/product")
public class ProductPageController {


    @GetMapping("/new")
    public String newProductPage(){
        return "new";
    }

    @GetMapping()
    public String productPage(){
        return "product";
    }

    @PostMapping("/changeProducktStatus/{slug}")
    public String handleChangeProducktStatus(){
        return "";
    }

}
