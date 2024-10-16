package dev.kush.inventoryservice.repo;

import dev.kush.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query(nativeQuery = true, value = "select case when count(i) > 0 then max(i.qty) >= :qty else false end from inventory i where i.sku = :sku")
    boolean isProductInStockBySku(String sku, int qty);
}