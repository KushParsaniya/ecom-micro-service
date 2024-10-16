package dev.kush.orderservice;

import dev.kush.orderservice.model.Order;
import dev.kush.orderservice.stubs.InventoryStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {

	@ServiceConnection
	private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer("postgres:latest");

	@LocalServerPort
	private Integer port;

	static {
		postgreSQLContainer.start();
	}

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void shouldCreateOrder() {
		Order order = getOrder(3);

		InventoryStub.stubInventoryCallTrue(order.getSku(), order.getQty());
		RestAssured.given()
				.contentType("application/json")
				.body(order)
				.when()
				.post("/api/v1/orders")
				.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.notNullValue(Long.class))
				.body("sku",Matchers.equalTo("macbook_m3"))
				.body("qty",Matchers.equalTo(3))
				.body("orderNo",Matchers.notNullValue(String.class));
	}

	@Test
	void shouldNotCreateOrder() {
		Order order = getOrder(11);
		InventoryStub.stubInventoryCallFalse(order.getSku(), order.getQty());

		RestAssured.given()
				.contentType("application/json")
				.body(order)
				.when()
				.post("/api/v1/orders")
				.then()
				.log().all()
				.statusCode(204);
	}

//	@Test
	void shouldReturn204() {
		InventoryStub.stubInventoryCallFalse("", 0);
		RestAssured.given()
				.contentType("application/json")
				.when()
				.post("/api/v1/orders")
				.then()
				.log().all()
				.statusCode(204);
	}

	private static Order getOrder(int qty) {
		return Order.builder()
				.sku("macbook_m3")
				.qty(qty)
				.build();
	}
}
