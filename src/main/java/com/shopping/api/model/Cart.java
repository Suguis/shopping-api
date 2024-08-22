package com.shopping.api.model;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Cart {

    @Size(max = 255)
    private String id;

}
