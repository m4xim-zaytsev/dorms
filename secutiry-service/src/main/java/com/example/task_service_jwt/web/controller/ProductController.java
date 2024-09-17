package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.entity.Product;
import com.example.task_service_jwt.entity.ProductCategory;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.security.SecurityService;
import com.example.task_service_jwt.service.impl.ProductCategoryServiceImpl;
import com.example.task_service_jwt.service.impl.ProductServiceImpl;
import com.example.task_service_jwt.web.model.request.ProductRequest;
import com.example.task_service_jwt.web.model.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductServiceImpl productService;
    private final ProductCategoryServiceImpl productCategoryService;
    private final SecurityService securityService; // Сервис для получения текущего пользователя

    @GetMapping("/new")
    public String newProductPage(){
        return "new";
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.findById(productId); // Получение товара
            ProductResponse productResponse = mapToProductResponse(product); // Преобразование в DTO
            return ResponseEntity.ok(productResponse);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Если товар не найден
        }
    }

    // Маппинг сущности Product в DTO ProductResponse
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .count(product.getCount())
                .sellerUsername(product.getSeller().getUsername()) // Продавец
                .categoryIds(product.getCategories().stream()
                        .map(ProductCategory::getId)
                        .collect(Collectors.toList())) // ID категорий
                .build();
    }

    // Страница редактирования товара
    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable Long id, Model model) {
        // Получаем данные о товаре по id
        Product product = productService.findById(id);
        if (product != null) {
            // Добавляем данные о товаре в модель
            model.addAttribute("product", product);
            return "new";
        } else {
            // Если товар не найден, перенаправляем на страницу ошибки или список товаров
            return "redirect:/api/v1/user";
        }
    }

    // Метод для обновления товара
//    @PostMapping("/update/{id}")
//    public String updateProduct(
//            @PathVariable Long id,
//            @ModelAttribute ProductRequest productRequest,
//            RedirectAttributes redirectAttributes) {
//        try {
//            // Получаем товар по id
//            Product product = productService.findById(id);
//
//            if (product != null) {
//                // Обновляем информацию о товаре
//                productService.updateProduct(id, productRequest);
//                redirectAttributes.addFlashAttribute("message", "Товар успешно обновлен");
//                return "redirect:/api/v1/user";
//            } else {
//                redirectAttributes.addFlashAttribute("error", "Товар не найден");
//                return "redirect:/api/v1/user";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении товара");
//            return "redirect:/api/v1/product/edit/" + id;
//        }
//    }

    @PostMapping("/add")
    public String addProduct(@AuthenticationPrincipal UserDetails userDetails,@ModelAttribute ProductRequest productRequest,
            RedirectAttributes redirectAttributes) {
        try {
            User seller = securityService.getUserByUsername(userDetails.getUsername());
            Product savedProduct = productService.addNewProduct(productRequest,seller);
            redirectAttributes.addFlashAttribute("message", "Товар успешно добавлен");
            return "redirect:/api/v1/user";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении товара");
            return "redirect:/api/v1/product/new";
        }
    }

    @GetMapping()
    public String productPage(){
        return "product";
    }

}
