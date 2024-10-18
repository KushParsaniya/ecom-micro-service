package dev.kush.notificationservice.service;

import dev.kush.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender javaMailSender;

    public void sendMail(OrderPlacedEvent orderPlacedEvent) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("kushparsaniya3@gmail.com");
            helper.setTo(orderPlacedEvent.email());
            helper.setSubject("Order Placed");
            helper.setText(String.format("""
                    Your order with order number %s is now placed successfully.
                    
                    Best Regards
                    Spring Shop
                    """, orderPlacedEvent.orderId()));
        };
        try {
            javaMailSender.send(mimeMessagePreparator);
            log.info("order placed mail send successfully to {}", orderPlacedEvent.email());
        } catch (MailException e) {
            log.error("Exception occurred when sending mail {}", e.getLocalizedMessage());
            throw new RuntimeException("Exception occurred when sending mail to springshop@email.com", e);
        }
    }
}
