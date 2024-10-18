package dev.kush.orderservice.service;

import dev.kush.event.OrderPlacedEvent;
import dev.kush.orderservice.clients.InventoryClient;
import dev.kush.orderservice.model.Order;
import dev.kush.orderservice.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public Order createOrder(Order order) {
        if (Objects.isNull(order)) {
            return null;
        }

        if (inventoryClient.isProductInStockBySku(order.getSku(), order.getQty())) {
            var savedOrder = orderRepository.save(order);
            log.info("kafka event publish start {}", order);
            kafkaTemplate.send("order-placed-new", new OrderPlacedEvent("kushparsaniya9@gmail.com", savedOrder.getOrderNo()));
            log.info("kafka event publish end");
            return savedOrder;
        }
        return null;
    }
}
