package com.capy.broker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka-bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin.NewTopics topics(KafkaAdmin admin) {
        admin.setAutoCreate(false);
        admin.setBootstrapServersSupplier(() -> bootstrapServers);
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name("capy-broker").build()
        );
    }
}
