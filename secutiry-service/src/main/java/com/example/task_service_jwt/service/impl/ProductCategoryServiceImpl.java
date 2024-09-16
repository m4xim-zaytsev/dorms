package com.example.task_service_jwt.service.impl;

import com.example.task_service_jwt.entity.ProductCategory;
import com.example.task_service_jwt.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl {

    private final ProductCategoryRepository categoryRepository;

    public List<ProductCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Set<ProductCategory> findCategoriesByIds(List<Long> categoryIds) {
        return new HashSet<>(categoryRepository.findAllById(categoryIds));
    }
}
