package com.example.task_service_jwt.service.impl;

import com.example.task_service_jwt.entity.Product;
import com.example.task_service_jwt.entity.ProductCategory;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.exception.EntityNotFoundException;
import com.example.task_service_jwt.repository.ProductCategoryRepository;
import com.example.task_service_jwt.repository.ProductRepository;
import com.example.task_service_jwt.security.SecurityService;
import com.example.task_service_jwt.service.ImageService;
import com.example.task_service_jwt.web.model.request.ProductRequest;
import com.example.task_service_jwt.web.model.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl {

    private final ProductRepository productRepository;
    private final ProductCategoryServiceImpl productCategoryService;
    private final ImageService imageService;
    private final SecurityService securityService; // Сервис для получения текущего пользователя

    public Product findById(Long id){
        return productRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException(MessageFormat.format("Product with id {} not found", id)));
    }

    public Product addNewProduct(ProductRequest productRequest, User user) throws IOException {

        User seller = user;

        Product product = new Product();
        product.setName(productRequest.getProductName());
        product.setPrice(productRequest.getProductPrice());
        product.setDescription(productRequest.getProductDescription());
        product.setCount(productRequest.getProductQuantity());
        product.setSeller(seller);

        Set<ProductCategory> categories = new HashSet<>(productCategoryService.findCategoriesByIds(productRequest.getProductCategory()));
        product.setCategories(categories);

        String imageUrl = imageService.saveImage(productRequest.getProductImage());
        product.setImageUrl(imageUrl);

        // Сохранение продукта
        return productRepository.save(product);
    }

    public ProductResponse updateProduckt(ProductRequest productRequest, Long id) {
        Product productToUpdate = findById(id);
        return null;
    }

    public Product updateProduct(Long id, ProductRequest productRequest) {
        return null;
    }
}
