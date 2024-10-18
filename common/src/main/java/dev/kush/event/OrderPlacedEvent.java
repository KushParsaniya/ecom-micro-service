package dev.kush.event;

public record OrderPlacedEvent(String email, String orderId) {
}