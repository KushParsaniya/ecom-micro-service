package dev.kush.inventoryservice.service;

import dev.kush.inventoryservice.repo.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isProductInStockBySku(String sku, int qty) {
        return inventoryRepository.isProductInStockBySku(sku,qty);
    }
}
