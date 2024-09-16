package com.example.task_service_jwt.repository;

import com.example.task_service_jwt.entity.CartItem;
import com.example.task_service_jwt.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductCategoryRepository  extends JpaRepository<ProductCategory,Long> {
}
