package dev.kush.inventoryservice.controller;

import dev.kush.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/stock")
    @ResponseStatus(HttpStatus.OK)
    boolean isProductInStockBySku(@RequestParam(required = false) String sku, @RequestParam(required = false) int qty) {
        if (sku == null || sku.isBlank()) {
            return false;
        }
        return inventoryService.isProductInStockBySku(sku,qty);
    }
}