package dev.kush.orderservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "inventory-service", url = "${inventory.url}", path = "/api/v1/inventory")
public interface InventoryClient {

    @GetMapping("/stock")
    boolean isProductInStockBySku(@RequestParam(required = false) String sku, @RequestParam(required = false) int qty);
}
