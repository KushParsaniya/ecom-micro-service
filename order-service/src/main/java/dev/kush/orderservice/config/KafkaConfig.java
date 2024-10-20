package dev.kush.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    NewTopic newTopic() {
        return TopicBuilder.name("order-placed-new")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
