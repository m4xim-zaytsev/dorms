package com.example.task_service_jwt.entity.dto;

import com.example.task_service_jwt.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Integer count;
    private String status;

    public static ProductDto fromEntity(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCount(),
                // Проверяем на null перед вызовом name()
                product.getStatus() != null ? product.getStatus().name() : "UNKNOWN" // Можно использовать значение по умолчанию
        );
    }

}
