package com.littlecrud.play.dto;
import java.util.List;

public record ValidationErrorResponse(
        String message,
        List<ValidationErrorDto> validationError
) {}