package com.shopping.api.model;

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
    private Long id;

    @NotNull
    private String description;

    @NotNull
    @Positive
    private Integer amount;
}
