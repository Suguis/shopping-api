package com.shopping.api.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cart {

    @Size(max = 255)
    private String id;

}
