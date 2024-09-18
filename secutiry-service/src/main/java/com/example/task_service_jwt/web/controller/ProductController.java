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



//    @GetMapping("/{productId}")
//    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
//
//    }
//
//
//    // Страница редактирования товара
//    @GetMapping("/edit/{id}")
//    public String editProductPage(@PathVariable Long id, Model model) {
//
//    }
//
//    // Метод для обновления товара
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
