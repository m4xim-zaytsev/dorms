package com.example.task_service_jwt.web.model.response.list;

import com.example.task_service_jwt.web.model.response.ProductCategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseList {
    private List<ProductCategoryResponse> bookings = new ArrayList<>();

}
