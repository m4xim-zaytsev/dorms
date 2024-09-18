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

    public Product addNewProduct(Product product, List<Long> categoryIds, MultipartFile productImage) throws IOException {
        // Обрабатываем категории
        Set<ProductCategory> categories = new HashSet<>(productCategoryService.findCategoriesByIds(categoryIds));
        product.setCategories(categories);

        // Сохраняем изображение
        String imageUrl = imageService.saveImage(productImage);
        product.setImageUrl(imageUrl);

        // Сохраняем продукт
        return productRepository.save(product);
    }


    public Product updateProduct(Product product, Long id, User seller, List<Long> categoryIds, MultipartFile productImage) throws IOException {
        // Находим существующий продукт по ID
        Product productToUpdate = findById(id);

        if (productToUpdate == null) {
            throw new EntityNotFoundException("Product with id " + id + " not found");
        }

        // Обновляем основные поля продукта
        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setCount(product.getCount());

        // Обновляем продавца только в случае, если текущий пользователь — продавец товара
        if (seller != null) {
            productToUpdate.setSeller(seller);
        }

        // Обновляем категории товара, если они были переданы
        if (categoryIds != null && !categoryIds.isEmpty()) {
            Set<ProductCategory> categories = new HashSet<>(productCategoryService.findCategoriesByIds(categoryIds));
            productToUpdate.setCategories(categories);
        }

        // Обновляем изображение, если оно было загружено
        if (productImage != null && !productImage.isEmpty()) {
            String imageUrl = imageService.saveImage(productImage);
            productToUpdate.setImageUrl(imageUrl);
        }

        // Сохраняем обновленный продукт в базе данных
        return productRepository.save(productToUpdate);
    }


}
