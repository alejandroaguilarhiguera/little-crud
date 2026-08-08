package com.littlecrud.play.controller;

import com.littlecrud.play.entity.Product;
import com.littlecrud.play.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.littlecrud.play.controller.ProductResponse;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * GET /products
     * Consulta todos los productos.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * GET /products/{id}
     * Consulta el detalle de un producto.
     */
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    /**
     * POST /products
     * Agrega un nuevo producto.
     */
    @PostMapping
    public ProductResponse createProduct(@Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ProductResponse("El producto fue creado correctamente", createdProduct);
    }

    /**
     * DELETE /products/{id}
     * Elimina un producto.
     */
    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable Integer id) {
        return productService.deleteProduct(id);
    }

    /**
     * DELETE /products/{id}/destroy
     * Elimina un producto de forma permanente.
     */
    @DeleteMapping("/{id}/destroy")
    public Map<String, String> destroyProduct(@PathVariable Integer id) {
        return productService.destroyProduct(id);
    }

    /**
     * PATCH /products/{id}
     * Actualiza parcialmente un producto.
     */
    @PatchMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable Integer id,
            @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return new ProductResponse("El producto fue actualizado correctamente", updatedProduct);
    }

    /**
     * PUT /products/{id}/restore
     * Restaura un producto eliminado.
     */
    @PutMapping("/{id}/restore")
    public ProductResponse restoreProduct(@PathVariable Integer id) {
        Product product = productService.restoreProduct(id);
        return new ProductResponse("El producto fue restaurado correctamente", product);
    }

    /**
     * GET /products/trashed
     * Consulta los productos eliminados.
     */
    @GetMapping("/trashed")
    public List<Product> getDeletedProducts() {
        return productService.getDeletedProducts();
    }
}