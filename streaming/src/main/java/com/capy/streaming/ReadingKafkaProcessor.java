package com.capy.streaming;

import com.capy.streaming.objects.DailyModel;
import com.capy.streaming.objects.ReadingModel;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Configuration
public class ReadingKafkaProcessor {

    @Bean
    public Function<KStream<Integer, ReadingModel>, KStream<Integer, DailyModel>> readingProcessor() {
        return kStream -> {
            java.util.Map<String, KStream<Integer, ReadingModel>> branchedKStream = kStream.split(Named.as("sensor-"))
                    .branch(
                            (id, readingModel) -> id == 1,
                            Branched.as("branch1")
                    )
                    .branch(
                            (id, readingModel) -> id == 2,
                            Branched.as("branch2")
                    )
                    .defaultBranch(
                            Branched.as("branch0")
                    );
            branchedKStream.get("branch0").groupByKey().aggregate(() -> new DailyModel(), (Aggregator<Integer, ReadingModel, ReadingModel>) (integer, readingModel, readingModel2) -> null).toStream();
            KGroupedStream<Integer, ReadingModel> streamPerSensor = kStream.groupByKey();
            return kStream;
        };
    }
}
