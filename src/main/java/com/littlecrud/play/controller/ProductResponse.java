package com.littlecrud.play.controller;
import com.littlecrud.play.entity.Product;

public record ProductResponse(
        String message,
        Product product
) {}