package com.capy.streaming;

import com.capy.streaming.objects.ReadingModel;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ReadingKafkaProcessor {

    @Bean
    public Function<KStream<Integer, ReadingModel>, KStream<Integer, ReadingModel>> readingProcessor() {
        return kstream -> {
            KGroupedStream<Integer, ReadingModel> streamPerSensor = kstream.groupByKey();
            kstream.filter((id, readingModel) -> {
                System.out.println(readingModel.label());
                return id < 5;
            });
            return kstream;
        };
    }
}
