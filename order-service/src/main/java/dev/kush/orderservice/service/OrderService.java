package dev.kush.orderservice.service;

import dev.kush.orderservice.clients.InventoryClient;
import dev.kush.orderservice.model.Order;
import dev.kush.orderservice.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public Order createOrder(Order order) {
        if (Objects.isNull(order)) {
            return null;
        }

        if (inventoryClient.isProductInStockBySku(order.getSku(), order.getQty())) {
            return orderRepository.save(order);
        }
        return null;
    }
}
