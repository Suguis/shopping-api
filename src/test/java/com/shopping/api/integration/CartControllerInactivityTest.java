package com.shopping.api.integration;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.shopping.api.config.AppConfig;
import com.shopping.api.repository.CartRepository;
import com.shopping.api.stub.ProductStubBuilder;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;

@SpringBootTest(properties = "cart.deletion.time=3000", webEnvironment = WebEnvironment.RANDOM_PORT)
public class CartControllerInactivityTest {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private CartRepository cartRepository;

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
    void shouldDeleteCartAfterInactivityPeriod() throws InterruptedException {
        var id = given().post("/carts").then().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract().path("id");

        var awaitTime = appConfig.cartDeletionTime().toMillis() * 2;
        Thread.sleep(awaitTime);

        given().get("/carts/" + id).then().assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldNotDeleteCartIfThereIsActivity() throws InterruptedException {
        var id = given().post("/carts").then().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract().path("id");

        var awaitTime = appConfig.cartDeletionTime().multipliedBy(2);
        var pollingTime = appConfig.cartDeletionTime().dividedBy(4);

        await().during(awaitTime).atMost(awaitTime.plusSeconds(2))
                .pollInterval(pollingTime)
                .untilAsserted(() -> {
                    var product = ProductStubBuilder.builder().build();
                    given().get("/carts/" + id).then().assertThat()
                            .statusCode(HttpStatus.OK.value());
                    given().contentType(ContentType.JSON).body(product).when()
                            .post("/carts/" + id + "/products")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.CREATED.value());
                });

        given().get("/carts/" + id).then().assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
