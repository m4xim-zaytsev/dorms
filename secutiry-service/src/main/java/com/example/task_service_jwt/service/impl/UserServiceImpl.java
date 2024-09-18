package com.example.task_service_jwt.service.impl;

import com.example.task_service_jwt.entity.Product;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.entity.dto.ProductDto;
import com.example.task_service_jwt.entity.dto.UserDetailsDto;
import com.example.task_service_jwt.exception.EntityNotFoundException;
import com.example.task_service_jwt.repository.ProductRepository;
import com.example.task_service_jwt.repository.UserRepository;
import com.example.task_service_jwt.utils.BeanUtils;
import com.example.task_service_jwt.web.model.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;

    public List<ProductDto> getProductsByUserId(Long userId) {
        // Получаем пользователя по id
        User user = findUserById(userId);

        // Получаем список продуктов этого пользователя
        List<Product> products = productRepository.findBySeller(user);

        // Преобразуем список продуктов в ProductDto
        return products.stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "User with username {} not found", username
                )));
    }

    public User updateUserDetails(User userToUpdate, UserDetailsDto dataToUserUpdate) {
        User user = userToUpdate;
        user.setName(dataToUserUpdate.getName());
        user.setEmail(dataToUserUpdate.getEmail());
        user.setUsername(dataToUserUpdate.getUsername());
        return userRepository.save(user);
    }

    public User saveUser(User currentUser) {
        return userRepository.save(currentUser);
    }
}
