package com.littlecrud.play.service;

import com.littlecrud.play.dto.ProductDto;
import com.littlecrud.play.entity.Product;
import com.littlecrud.play.exception.ProductNotFoundException;
import com.littlecrud.play.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;


    @Test
    void shouldGetAllProducts() {

        Product product = new Product();

        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setStock(10);

        Page<Product> page = new PageImpl<>(
                List.of(product)
        );

        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByDeletedAtIsNull(pageable))
                .thenReturn(page);

        Page<Product> result =
                productService.getAllProducts(pageable);

        assertThat(result.getContent())
                .hasSize(1);

        assertThat(result.getContent().getFirst().getName())
                .isEqualTo("Laptop");

        verify(productRepository)
                .findByDeletedAtIsNull(pageable);
    }


    @Test
    void shouldGetProductById() {

        Product product = new Product();

        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setStock(10);

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        Product result =
                productService.getProductById(1);

        assertThat(result)
                .isNotNull();

        assertThat(result.getId())
                .isEqualTo(1);

        assertThat(result.getName())
                .isEqualTo("Laptop");

        verify(productRepository)
                .findById(1);
    }


    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {

        when(productRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> productService.getProductById(999)
        )
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Producto no encontrado");

        verify(productRepository)
                .findById(999);
    }


    @Test
    void shouldCreateProduct() {

        ProductDto dto = new ProductDto(
                "Laptop",
                1500.0,
                10
        );

        Product savedProduct = new Product();

        savedProduct.setId(1);
        savedProduct.setName("Laptop");
        savedProduct.setPrice(1500.0);
        savedProduct.setStock(10);

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        Product result =
                productService.createProduct(dto);

        assertThat(result.getId())
                .isEqualTo(1);

        assertThat(result.getName())
                .isEqualTo("Laptop");

        assertThat(result.getPrice())
                .isEqualTo(1500.0);

        assertThat(result.getStock())
                .isEqualTo(10);

        verify(productRepository)
                .save(any(Product.class));
    }


    @Test
    void shouldDeleteProduct() {

        Product product = new Product();

        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setStock(10);

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        when(productRepository.save(product))
                .thenReturn(product);

        Map<String, String> result =
                productService.deleteProduct(1);

        assertThat(result)
                .containsEntry(
                        "message",
                        "Producto eliminado"
                );

        assertThat(product.getDeletedAt())
                .isNotNull();

        verify(productRepository)
                .findById(1);

        verify(productRepository)
                .save(product);
    }


    @Test
    void shouldDestroyProduct() {

        Product product = new Product();

        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setStock(10);

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        Map<String, String> result =
                productService.destroyProduct(1);

        assertThat(result)
                .containsEntry(
                        "message",
                        "Producto eliminado de manera permanente"
                );

        verify(productRepository)
                .findById(1);

        verify(productRepository)
                .delete(product);
    }


    @Test
    void shouldUpdateProduct() {

        Product product = new Product();

        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setStock(10);

        ProductDto dto = new ProductDto(
                "Laptop Pro",
                2000.0,
                20
        );

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        when(productRepository.save(product))
                .thenReturn(product);

        Product result =
                productService.updateProduct(1, dto);

        assertThat(result.getName())
                .isEqualTo("Laptop Pro");

        assertThat(result.getPrice())
                .isEqualTo(2000.0);

        assertThat(result.getStock())
                .isEqualTo(20);

        verify(productRepository)
                .findById(1);

        verify(productRepository)
                .save(product);
    }


    @Test
    void shouldUpdateOnlyProvidedFields() {

        Product product = new Product();

        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setStock(10);

        ProductDto dto = new ProductDto(
                "Laptop Pro",
                null,
                null
        );

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        when(productRepository.save(product))
                .thenReturn(product);

        Product result =
                productService.updateProduct(1, dto);

        assertThat(result.getName())
                .isEqualTo("Laptop Pro");

        assertThat(result.getPrice())
                .isEqualTo(1500.0);

        assertThat(result.getStock())
                .isEqualTo(10);

        verify(productRepository)
                .save(product);
    }


    @Test
    void shouldRestoreProduct() {

        Product product = new Product();

        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setStock(10);
        product.setDeletedAt(LocalDateTime.now());

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        when(productRepository.save(product))
                .thenReturn(product);

        Product result =
                productService.restoreProduct(1);

        assertThat(result.getDeletedAt())
                .isNull();

        verify(productRepository)
                .findById(1);

        verify(productRepository)
                .save(product);
    }


    @Test
    void shouldGetDeletedProducts() {

        Product product = new Product();

        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setStock(10);
        product.setDeletedAt(LocalDateTime.now());

        Page<Product> page = new PageImpl<>(
                List.of(product)
        );

        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findByDeletedAtIsNotNull(pageable))
                .thenReturn(page);

        Page<Product> result =
                productService.getDeletedProducts(pageable);

        assertThat(result.getContent())
                .hasSize(1);

        assertThat(result.getContent().getFirst().getDeletedAt())
                .isNotNull();

        verify(productRepository)
                .findByDeletedAtIsNotNull(pageable);
    }
}