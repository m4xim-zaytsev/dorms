package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.entity.Product;
import com.example.task_service_jwt.entity.ProductCategory;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.security.SecurityService;
import com.example.task_service_jwt.service.impl.ProductCategoryServiceImpl;
import com.example.task_service_jwt.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductPageController {

    private final ProductServiceImpl productService;
    private final ProductCategoryServiceImpl productCategoryService;
    private final SecurityService securityService; // Сервис для получения текущего пользователя

    @GetMapping("/new")
    public String newProductPage(){
        return "new";
    }

    @PostMapping("/add")
    public String addProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("productName") String productName,
            @RequestParam("productPrice") double productPrice,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("productQuantity") int productQuantity,
            @RequestParam("productCategory") List<Long> productCategoryIds,
            @RequestParam("productImage") MultipartFile productImage,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User seller = securityService.getUserByUsername(userDetails.getUsername());
            Product savedProduct = productService.addNewProduct(productName, productPrice, productDescription, productQuantity, productCategoryIds, productImage, seller);
            redirectAttributes.addFlashAttribute("message", "Товар успешно добавлен");
            return "redirect:/api/v1/profile";
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
