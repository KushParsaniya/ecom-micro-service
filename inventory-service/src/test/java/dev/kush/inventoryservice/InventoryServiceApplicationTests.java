package dev.kush.inventoryservice;

import dev.kush.inventoryservice.model.Inventory;
import dev.kush.inventoryservice.repo.InventoryRepository;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

    @ServiceConnection
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @LocalServerPort
    private Integer port;

    @Autowired
    private InventoryRepository inventoryRepository;
    
    static {
        postgreSQLContainer.start();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        inventoryRepository.save(new Inventory(1L,"macbook_3",10));
    }

    @Test
    void shouldReturnTrue() {
        RestAssured.given()
                .param("sku","macbook_3")
                .param("qty",10)
                .when()
                .get("/api/v1/inventory/stock")
                .then()
                .log().all()
                .statusCode(200)
                .body(Matchers.equalTo("true"));
    }

    @Test
    void shouldReturnFalse() {
        RestAssured.given()
                .param("sku","macbook_3")
                .param("qty",11)
                .when()
                .get("/api/v1/inventory/stock")
                .then()
                .log().all()
                .statusCode(200)
                .body(Matchers.equalTo("false"));
    }

    @Test
    void shouldReturnFalseWhenSkuIsNotPresent() {
        RestAssured.given()
                .param("qty",10)
                .when()
                .get("/api/v1/inventory/stock")
                .then()
                .log().all()
                .statusCode(200)
                .body(Matchers.equalTo("false"));
    }
}
