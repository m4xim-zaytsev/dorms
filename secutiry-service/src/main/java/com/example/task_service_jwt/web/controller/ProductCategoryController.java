package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.entity.ProductCategory;
import com.example.task_service_jwt.service.impl.ProductCategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class ProductCategoryController {

    private final ProductCategoryServiceImpl productCategoryService;

    @GetMapping
    public List<ProductCategory> getCategories() {
        return productCategoryService.getAllCategories();
    }
}
