package com.example.task_service_jwt.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public enum ProductStatus {
        AVAILABLE, SOLD
    }
}
