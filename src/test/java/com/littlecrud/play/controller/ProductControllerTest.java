package com.littlecrud.play.controller;

import com.littlecrud.play.dto.ProductDto;
import com.littlecrud.play.entity.Product;
import com.littlecrud.play.exception.ProductNotFoundException;
import com.littlecrud.play.service.ProductService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;


    @Test
    void shouldGetProductById() throws Exception {

        Product product = new Product();

        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setStock(10);

        when(productService.getProductById(1))
                .thenReturn(product);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1500.0))
                .andExpect(jsonPath("$.stock").value(10));
    }


    @Test
    void shouldReturn404WhenProductDoesNotExist() throws Exception {

        when(productService.getProductById(999))
                .thenThrow(
                        new ProductNotFoundException("Producto no encontrado")
                );

        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldCreateProduct() throws Exception {

        String json = """
                {
                    "name": "Laptop",
                    "price": 1500.0,
                    "stock": 10
                }
                """;

        Product product = new Product();

        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setStock(10);

        when(productService.createProduct(any(ProductDto.class)))
                .thenReturn(product);

        mockMvc.perform(
                        post("/products")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.message")
                                .value("El producto fue creado correctamente")
                )
                .andExpect(jsonPath("$.product.id").value(1))
                .andExpect(jsonPath("$.product.name").value("Laptop"));
    }


    @Test
    void shouldRejectInvalidPrice() throws Exception {

        String json = """
                {
                    "name": "Laptop",
                    "price": -100,
                    "stock": 10
                }
                """;

        mockMvc.perform(
                        post("/products")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldUpdateProduct() throws Exception {

        String json = """
                {
                    "name": "Laptop actualizada",
                    "price": 1800.0,
                    "stock": 20
                }
                """;

        Product product = new Product();

        product.setId(1);
        product.setName("Laptop actualizada");
        product.setPrice(1800.0);
        product.setStock(20);

        when(productService.updateProduct(
                eq(1),
                any(ProductDto.class)
        )).thenReturn(product);

        mockMvc.perform(
                        patch("/products/1")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.message")
                                .value("El producto fue actualizado correctamente")
                )
                .andExpect(
                        jsonPath("$.product.name")
                                .value("Laptop actualizada")
                );
    }


    @Test
    void shouldDeleteProduct() throws Exception {

        when(productService.deleteProduct(1))
                .thenReturn(
                        java.util.Map.of(
                                "message",
                                "Producto eliminado"
                        )
                );

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.message")
                                .value("Producto eliminado")
                );
    }


    @Test
    void shouldDestroyProduct() throws Exception {

        when(productService.destroyProduct(1))
                .thenReturn(
                        java.util.Map.of(
                                "message",
                                "Producto eliminado de manera permanente"
                        )
                );

        mockMvc.perform(delete("/products/1/destroy"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.message")
                                .value("Producto eliminado de manera permanente")
                );
    }


    @Test
    void shouldRestoreProduct() throws Exception {

        Product product = new Product();

        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setStock(10);

        when(productService.restoreProduct(1))
                .thenReturn(product);

        mockMvc.perform(put("/products/1/restore"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.message")
                                .value("El producto fue restaurado correctamente")
                )
                .andExpect(jsonPath("$.product.id").value(1));
    }
}