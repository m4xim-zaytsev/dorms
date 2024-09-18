package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.entity.Product;
import com.example.task_service_jwt.entity.ProductCategory;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.entity.dto.ProductDto;
import com.example.task_service_jwt.mapper.ProductMapper;
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
    private final SecurityService securityService;
    private final ProductMapper productMapper;

    @GetMapping("/new")
    public String newProductPage(){
        return "new";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable Long id,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Product product = productService.findById(id);
        // Получаем товар по ID
        ProductDto productDto = ProductDto.fromEntity(product);

        // Получаем пользователя, который делает запрос
        User seller = securityService.getUserByUsername(userDetails.getUsername());

        // Проверяем, является ли пользователь продавцом товара
        if (!product.getSeller().getUsername().equals(seller.getUsername())) {
            // Если пользователь не является продавцом, перенаправляем его с ошибкой
            redirectAttributes.addFlashAttribute("error", "Вы не можете редактировать этот товар");
            return "redirect:/api/v1/user/my";
        }

        // Добавляем товар в модель, чтобы передать на фронтенд
        model.addAttribute("product", productDto);

        // Также передаем категории товара для выпадающего списка
        model.addAttribute("categories", productCategoryService.getAllCategories());

        return "edit-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute ProductRequest productRequest,
            RedirectAttributes redirectAttributes) {

        try {
            // Преобразование запроса в сущность Product
            Product product = productMapper.requestToProduct(productRequest);

            // Получение информации о пользователе, который делает запрос
            User seller = securityService.getUserByUsername(userDetails.getUsername());

            // Проверяем, что текущий пользователь является продавцом товара
            Product existingProduct = productService.findById(id);
            if (!existingProduct.getSeller().getUsername().equals(seller.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "Вы не можете обновить этот товар");
                return "redirect:/api/v1/product/edit/" + id;
            }

            // Обновление товара с категориями и изображением
            productService.updateProduct(product, id, seller, productRequest.getProductCategory(), productRequest.getProductImage());

            // В случае успеха перенаправляем на страницу пользователя
            redirectAttributes.addFlashAttribute("message", "Товар успешно обновлен");
            return "redirect:/api/v1/user/my";
        } catch (Exception e) {
            // Логирование ошибки для отладки
            e.printStackTrace();

            // В случае ошибки перенаправляем на страницу редактирования с сообщением об ошибке
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении товара");
            return "redirect:/api/v1/product/edit/" + id;
        }
    }





    @PostMapping("/add")
    public String addProduct(@AuthenticationPrincipal UserDetails userDetails,
                             @ModelAttribute ProductRequest productRequest,
                             RedirectAttributes redirectAttributes) {
        try {
            User seller = securityService.getUserByUsername(userDetails.getUsername());

            // Используем ProductMapper для преобразования ProductRequest в Product
            Product product = productMapper.requestToProduct(productRequest);
            product.setSeller(seller);  // Устанавливаем продавца

            // Сохраняем продукт с использованием сервиса
            Product savedProduct = productService.addNewProduct(product, productRequest.getProductCategory(), productRequest.getProductImage());

            redirectAttributes.addFlashAttribute("message", "Товар успешно добавлен");
            return "redirect:/api/v1/user/my";
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
