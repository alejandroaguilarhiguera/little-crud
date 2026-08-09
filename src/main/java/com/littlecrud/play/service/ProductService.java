package com.littlecrud.play.service;

import com.littlecrud.play.dto.ProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.littlecrud.play.entity.Product;
import com.littlecrud.play.exception.ProductNotFoundException;
import com.littlecrud.play.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private Product findById(Integer id) {
        return this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));
    }
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    /**
     * Consulta todos los productos.
     */
    public Page<Product> getAllProducts(Pageable pageable) {
        return this.productRepository.findByDeletedAtIsNull(pageable);
    }

    /**
     * Consulta un producto por su ID.
     */
    public Product getProductById(Integer id) {
        return findById(id);
    }

    /**
     * Agrega un nuevo producto.
     */
    public Product createProduct(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setStock(dto.stock());
        return this.productRepository.save(product);
    }

    /**
     * Elimina un producto.
     */
    public Map<String, String> deleteProduct(Integer id) {
        Product product = findById(id);
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
        return Map.of("message", "Producto eliminado");
    }

    public Map<String, String> destroyProduct(Integer id) {
        Product product = findById(id);
        productRepository.delete(product);
        return Map.of("message", "Producto eliminado de manera permanente");
    }

    /**
     * Actualiza parcialmente un producto.
     */
    public Product updateProduct(Integer id, ProductDto dto) {
        Product productUpdated = findById(id);

        Optional.ofNullable(dto.name())
                .ifPresent(productUpdated::setName);

        Optional.ofNullable(dto.stock())
                .ifPresent(productUpdated::setStock);

        Optional.ofNullable(dto.price())
                .ifPresent(productUpdated::setPrice);
        return this.productRepository.save(productUpdated);
    }

    /**
     * Restaura un producto eliminado.
     */
    public Product restoreProduct(Integer id) {
        Product productUpdated = findById(id);
        productUpdated.setDeletedAt(null);
        return this.productRepository.save(productUpdated);
    }

    /**
     * Consulta los productos eliminados.
     */
    public Page<Product> getDeletedProducts(Pageable pageable) {
        return this.productRepository.findByDeletedAtIsNotNull(pageable);
    }
}