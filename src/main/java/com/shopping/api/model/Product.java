package com.shopping.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
