package com.example.task_service_jwt.mapper;

import com.example.task_service_jwt.entity.ProductCategory;
import com.example.task_service_jwt.web.model.request.ProductCategoryRequest;
import com.example.task_service_jwt.web.model.request.ProductRequest;
import com.example.task_service_jwt.web.model.response.ProductCategoryResponse;
import com.example.task_service_jwt.web.model.response.list.CategoryResponseList;
import jdk.jfr.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductCategoryMapper {

    // Преобразование ProductCategory в ProductCategoryResponse
    ProductCategoryResponse categoryToResponse(ProductCategory category);

    // Преобразование ProductCategoryRequest в ProductCategory
        ProductCategory requestToCategory(ProductCategoryRequest request);

    // Преобразование списка категорий в CategoryResponseList
    default CategoryResponseList categoryListToCategoryResponseList(List<ProductCategory> categories) {
        List<ProductCategoryResponse> categoryResponses = categories.stream()
                .map(this::categoryToResponse)
                .collect(Collectors.toList());

        return new CategoryResponseList(categoryResponses);
    }

}

