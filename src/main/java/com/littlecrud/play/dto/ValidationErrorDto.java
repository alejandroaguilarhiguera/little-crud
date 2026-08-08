package com.littlecrud.play.dto;

public record ValidationErrorDto(
        String field,
        Object value,
        String message
) {}