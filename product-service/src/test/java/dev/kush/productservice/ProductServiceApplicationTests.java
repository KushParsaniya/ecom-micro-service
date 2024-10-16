package dev.kush.productservice;

import dev.kush.productservice.model.Product;
import io.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

import java.math.BigDecimal;
import java.util.Optional;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mongoDBContainer.start();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        Product product = getProduct();

        RestAssured.given()
                .contentType("application/json")
                .body(product)
                .when()
                .post("/api/v1/products")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("sku", Matchers.equalTo("macbook_m3"))
                .body("qty",Matchers.equalTo(10));
    }

    private Product getProduct() {
        return Product
                .builder()
                .sku("macbook_m3")
                .qty(10)
                .price(BigDecimal.valueOf(10000))
                .build();
    }

    @Test
    void shouldReturn204() {
        RestAssured.given()
                .contentType("application/json")
                .when()
                .post("/api/v1/products")
                .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    void shouldReturnSavedProduct() {
        var product = getProduct();

        RestAssured.given()
                .contentType("application/json")
                .body(product)
                .when()
                .post("/api/v1/products");


        RestAssured.given()
                .when()
                .get("/api/v1/products")
                .then()
                .log().all()
                .body("size()",Matchers.equalTo(1))
                .body("[0].id",Matchers.notNullValue())
                .body("[0].sku", Matchers.equalTo("macbook_m3"))
                .body("[0].qty",Matchers.equalTo(10));
    }

    @Test
    void shouldReturnEmptyList() {
        RestAssured.given()
                .when()
                .get("/api/v1/products")
                .then()
                .log().all()
                .body("size()",Matchers.equalTo(0));
    }

}
