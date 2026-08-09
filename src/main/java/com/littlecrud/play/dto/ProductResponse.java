package com.littlecrud.play.dto;

import com.littlecrud.play.entity.Product;

public record ProductResponse(
        String message,
        Product product
) {}