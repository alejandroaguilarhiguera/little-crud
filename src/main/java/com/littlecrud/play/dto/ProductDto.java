package com.littlecrud.play.dto;

public record ProductDto(
        Integer id,
        String name,
        Double price,
        Integer stock,
        String deleted_at
) {}