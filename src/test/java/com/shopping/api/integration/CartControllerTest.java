package com.shopping.api.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import com.shopping.api.service.CartService;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class CartControllerTest {

    @Autowired
    private CartService cartService;

    @Test
    void shouldCreate() {
        given().post("/api/carts").then().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", not(nullValue()))
                .body("products", isA(List.class))
                .body("products", hasSize(0));
    }

    @Test
    void shouldGet() {
        var cart = cartService.create();

        given().get("/api/carts/" + cart.getId()).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/json")
                .body("id", equalTo(cart.getId().toString()))
                .body("products", isA(List.class))
                .body("products", hasSize(0));
    }

    @Test
    void shouldGetNotFoundIfCartDoesNotExist() {
        given().get("/api/carts/" + UUID.randomUUID()).then().assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(equalTo(""));
    }
}
