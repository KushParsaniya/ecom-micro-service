package dev.kush.orderservice.stubs;

import lombok.experimental.UtilityClass;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@UtilityClass
public class InventoryStub {

    public void stubInventoryCallTrue(String sku, int qty) {
        stubFor(get(urlEqualTo("/api/v1/inventory/stock?sku=" + sku + "&qty=" + qty))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("true")));
    }

    public void stubInventoryCallFalse(String sku, int qty) {
        stubFor(get(urlEqualTo("/api/v1/inventory/stock?sku=" + sku + "&qty=" + qty))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("false")));
    }

}
