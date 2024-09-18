package com.example.task_service_jwt.web.model.request;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    private String productName;
    private Double productPrice;
    private String productDescription;
    private Integer productQuantity;
    private List<Long> productCategory;
    private MultipartFile productImage;

}
