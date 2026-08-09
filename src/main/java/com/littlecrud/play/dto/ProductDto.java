package com.littlecrud.play.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductDto(

        @Size(max = 100, message = "El nombre no puede ser mayor a 100 caracteres")
        String name,

        @Positive(message = "El precio debe ser mayor a 0")
        Double price,

        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock

) {}