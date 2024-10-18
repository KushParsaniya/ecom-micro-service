package dev.kush.notificationservice.consumer;

import dev.kush.event.OrderPlacedEvent;
import dev.kush.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "order-placed-new")
    public void listen(OrderPlacedEvent orderPlacedEvent) {
        notificationService.sendMail(orderPlacedEvent);
    }
}
