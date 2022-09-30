package com.capy.dbconnector;

import com.capy.dbconnector.objects.DailyModel;
import com.capy.dbconnector.objects.ReadingModel;
import com.capy.dbconnector.serializer.DailyDeserializer;
import com.capy.dbconnector.serializer.ReadingDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka-bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> consumerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-influx");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DailyDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<Long, DailyModel> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new LongDeserializer(), new DailyDeserializer());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Long, DailyModel>> dailyFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, DailyModel> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    public Map<String, Object> lateConsumerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-influx");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ReadingDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<Long, ReadingModel> lateConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(lateConsumerConfig(), new LongDeserializer(), new ReadingDeserializer());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Long, ReadingModel>> lateFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, ReadingModel> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(lateConsumerFactory());
        return factory;
    }
}
