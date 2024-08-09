package com.productService.app.service;

import com.productService.app.dto.ProductDto;
import com.productService.app.model.Product;
import com.productService.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductDto productDto){
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .build();

        productRepository.save(product);
        System.out.println("product " + product.getId() + " saved");
    }

    public List<ProductDto> getAllProducts(){
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> mapToProductDto(product)).toList();
    }

    private ProductDto mapToProductDto(Product product){
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
