package com.example.task_service_jwt.mapper;

import com.example.task_service_jwt.entity.Product;
import com.example.task_service_jwt.web.model.request.ProductRequest;
import com.example.task_service_jwt.web.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "name", source = "productName")
    @Mapping(target = "price", source = "productPrice")
    @Mapping(target = "description", source = "productDescription")
    @Mapping(target = "count", source = "productQuantity")
    @Mapping(target = "categories", source = "productCategory", ignore = true) // Отдельно мапим категории
    @Mapping(target = "imageUrl", ignore = true) // imageUrl устанавливается вручную после загрузки
    @Mapping(target = "seller", ignore = true) // Устанавливается на уровне бизнес-логики
    Product requestToProduct(ProductRequest request);

    @Mapping(target = "productName", source = "name")
    @Mapping(target = "productPrice", source = "price")
    @Mapping(target = "productDescription", source = "description")
    @Mapping(target = "productQuantity", source = "count")
    @Mapping(target = "productCategory", ignore = true) // Маппинг категорий делается отдельно
    @Mapping(target = "productImage", ignore = true) // Image не нужно возвращать в запросе
    ProductRequest productToRequest(Product product);

    @Mapping(target = "sellerId", source = "seller.id")
    ProductResponse productToResponse(Product product);

    List<ProductResponse> productsToResponses(List<Product> products);
}

