package com.shopping.api.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.api.repository.CartRepository;
import com.shopping.api.service.CartService;
import com.shopping.api.stub.ProductStubBuilder;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CartControllerTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @LocalServerPort
    private Integer port;

    @PostConstruct
    void setUpPort() {
        RestAssured.port = port;
    }

    @BeforeEach
    void clearDatabase() {
        cartRepository.deleteAll();
    }

    @Test
    void shouldCreate() {
        given().post("/carts").then().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", not(nullValue()))
                .body("products", isA(List.class))
                .body("products", hasSize(0));
    }

    @Test
    void shouldGet() {
        var cart = cartService.create();

        given().get("/carts/" + cart.getId().get()).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(cart.getId().get().toString()))
                .body("products", isA(List.class))
                .body("products", hasSize(0));
    }

    @Test
    void shouldReceiveNotFoundOnGetWhenNonExistentCart() {
        given().get("/carts/" + UUID.randomUUID()).then().assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(emptyString());
    }

    @Test
    void shouldDeleteExistingCart() {
        var cart = cartService.create();

        given().delete("/carts/" + cart.getId().get()).then().assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .body(emptyString());

        assertTrue(cartService.get(cart.getId().get()).isEmpty());
    }

    @Test
    void shouldReceiveNotFoundOnDeleteWhenNonExistentCart() {
        given().delete("/carts/" + UUID.randomUUID()).then().assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(emptyString());
    }

    @Test
    void shouldAddProductToCart() {
        var cart = cartService.create();
        var product = ProductStubBuilder.builder().build();

        given().contentType(ContentType.JSON).body(product).when()
                .post("/carts/" + cart.getId().get() + "/products")
                .then().assertThat().statusCode(HttpStatus.CREATED.value())
                .body(emptyString());

        assertTrue(cartService.get(cart.getId().get()).get().getProducts().contains(product));
    }

    @Test
    void shouldGetProductAfterBeingAdded() throws JsonProcessingException {
        var previousCart = cartService.create();
        var product = ProductStubBuilder.builder().build();
        var productMap = mapper.readValue(mapper.writeValueAsString(product), Map.class);

        cartService.addProduct(previousCart.getId().get(), product);

        given().get("/carts/" + previousCart.getId().get()).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("products", contains(productMap))
                .body("products", hasSize(previousCart.getProducts().size() + 1));
    }

    @Test
    void shouldReceiveNotFoundWhenTryingToAddProductOnNonExistentCart() {
        var product = ProductStubBuilder.builder().build();

        given().contentType(ContentType.JSON).body(product).when().post("/carts/" + UUID.randomUUID() + "/products")
                .then().assertThat().statusCode(HttpStatus.NOT_FOUND.value())
                .body(emptyString());
    }

    @Test
    void shouldReceiveBadRequestWhenProductIsMalformed() {
        var cart = cartService.create();
        var product = ProductStubBuilder.builder().amount(-1).build();

        given().contentType(ContentType.JSON).body(product).when()
                .post("/carts/" + cart.getId().get() + "/products")
                .then().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReceiveConflictWhenProductAlreadyExists() {
        var cartId = cartService.create().getId().get();
        var repeatedProduct = ProductStubBuilder.builder().id(1L).build();

        cartService.addProduct(cartId, ProductStubBuilder.builder().id(1L).build());

        given().contentType(ContentType.JSON).body(repeatedProduct).when().post("/carts/" + cartId + "/products")
                .then().assertThat().statusCode(HttpStatus.CONFLICT.value())
                .body(emptyString());
    }

    @Test
    void shouldDeleteAfterCartDeletionTime() {
        var cartId = cartService.create().getId().get();
        var repeatedProduct = ProductStubBuilder.builder().id(1L).build();

        cartService.addProduct(cartId, ProductStubBuilder.builder().id(1L).build());

        given().contentType(ContentType.JSON).body(repeatedProduct).when().post("/carts/" + cartId + "/products")
                .then().assertThat().statusCode(HttpStatus.CONFLICT.value())
                .body(emptyString());
    }

}
