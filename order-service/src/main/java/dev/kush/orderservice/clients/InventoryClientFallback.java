package dev.kush.orderservice.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InventoryClientFallback implements InventoryClient {

    @Override
    public boolean isProductInStockBySku(String sku, int qty) {
        log.error("Service Unavailable, using fallback method sku: {}, qty: {}", sku, qty);
        return false;
    }
}
