package com.example.task_service_jwt.service.impl;

import com.example.task_service_jwt.entity.Product;
import com.example.task_service_jwt.entity.ProductCategory;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.repository.ProductCategoryRepository;
import com.example.task_service_jwt.repository.ProductRepository;
import com.example.task_service_jwt.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl {

    private final ProductRepository productRepository;
    private final ProductCategoryServiceImpl productCategoryService;
    private final ImageService imageService;

    public Product addNewProduct(String name, Double price, String description,
                                 Integer quantity, List<Long> categoryIds, MultipartFile image, User seller) throws IOException {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setCount(quantity);
        product.setSeller(seller);

        Set<ProductCategory> categories = new HashSet<>(productCategoryService.findCategoriesByIds(categoryIds));
        product.setCategories(categories);

        String imageUrl = imageService.saveImage(image);
        product.setImageUrl(imageUrl);


        // Сохранение продукта
        return productRepository.save(product);
    }

}
