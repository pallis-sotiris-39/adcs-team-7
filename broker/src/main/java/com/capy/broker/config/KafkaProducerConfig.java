package com.capy.broker.config;

import com.capy.broker.objects.ReadingModel;
import com.capy.broker.serializer.ReadingSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka-bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> producerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ReadingSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<Long, ReadingModel> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig(), new LongSerializer(), new ReadingSerializer());
    }

    @Bean
    public KafkaTemplate<Long, ReadingModel> kafkaTemplate(ProducerFactory<Long, ReadingModel> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
