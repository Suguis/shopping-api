package com.shopping.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    private Long id;

    @NotNull
    private String description;

    @NotNull
    private Integer amount;

    @Valid
    private Cart cart;

}
