package dev.kush.orderservice.controller;

import dev.kush.orderservice.model.Order;
import dev.kush.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody(required = false) Order order) {
        var savedOrder = orderService.createOrder(order);

        if (savedOrder == null) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }
}
