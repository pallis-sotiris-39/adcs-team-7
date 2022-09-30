package com.capy.streaming.config;

import com.capy.streaming.serializer.DailyModelSerdes;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;

import static org.apache.kafka.streams.StreamsConfig.*;

@Configuration
@ComponentScan(basePackages = {"com.capy.streaming"})
@EnableKafkaStreams
public class KafkaStreamConfig {
    @Value("${spring.kafka-bootstrap-servers}")
    private String bootstrapServers;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfig () {
        HashMap<String, Object> props = new HashMap<>();
        props.put(APPLICATION_ID_CONFIG, "capy-streaming");
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, DailyModelSerdes.class.getName());
        return new KafkaStreamsConfiguration(props);
    }
}
