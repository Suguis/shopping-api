package com.shopping.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Product {

    @NotNull
    @Positive
    @Schema(description = "Unique identifier for the product", required = true, minimum = "1")
    private Long id;

    @NotNull
    @Schema(description = "Description of the product", required = true, minimum = "1")
    private String description;

    @NotNull
    @Positive
    @Schema(description = "The amount of this product", required = true, minimum = "1")
    private Integer amount;
}
