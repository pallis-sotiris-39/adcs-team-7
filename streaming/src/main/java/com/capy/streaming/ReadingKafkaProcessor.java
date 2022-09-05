package com.capy.streaming;

import com.capy.streaming.objects.DailyModel;
import com.capy.streaming.objects.ReadingModel;
import com.capy.streaming.serializer.DailyModelSerde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class ReadingKafkaProcessor {

    @Bean
    public Function<KStream<Long, ReadingModel>, KStream<Long, DailyModel>> readingProcessor() {
        return kStream -> {
            Map<String, KStream<Long, ReadingModel>> branchedKStream = kStream.split(Named.as("sensor-"))
                    .branch((id, readingModel) -> readingModel.sensorId() == 1)
                    .branch((id, readingModel) -> readingModel.sensorId() == 2)
                    .defaultBranch();
            Map<String, KStream<Long, DailyModel>> newKStreams = new HashMap<>();
            branchedKStream.forEach(
                    (s, stream) -> newKStreams.put(
                            s,
                            stream
                                    .map((timestamp, readingModel) -> KeyValue.pair(getDayTimestampFromTimestamp(timestamp), readingModel))
                                    .groupByKey().aggregate(
                                            DailyModel::new,
                                            (aLong, readingModel, dailyModel) -> new DailyModel(
                                                    dailyModel.sensorId,
                                                    dailyModel.sensorType,
                                                    dailyModel.label,
                                                    Float.min(readingModel.value(), dailyModel.min),
                                                    Float.max(readingModel.value(), dailyModel.max),
                                                    getAvgValue(readingModel, dailyModel),
                                                    dailyModel.sum + readingModel.value(),
                                                    dailyModel.total + 1
                                            ),
                                    Materialized.<Long, DailyModel, KeyValueStore<Bytes, byte[]>>as(
                                            DailyModel.class.getName()
                                    ).withValueSerde(new DailyModelSerde())
                                    ).toStream()
                    )
            );
            KStream<Long, DailyModel> result = newKStreams.get("sensor-0").merge(newKStreams.get("sensor-1")).merge(newKStreams.get("sensor-2"));
            return result;
        };
    }

    private float getAvgValue(ReadingModel readingModel, DailyModel dailyModel) {
        return (dailyModel.sum + readingModel.value()) / (dailyModel.total + 1);
    }

    private Long getDayTimestampFromTimestamp(Long timestamp) {
        return (timestamp / 86400L) * 86400L;
    }
}
