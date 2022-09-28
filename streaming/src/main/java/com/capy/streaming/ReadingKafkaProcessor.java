package com.capy.streaming;

import com.capy.streaming.objects.DailyModel;
import com.capy.streaming.objects.ReadingModel;
import com.capy.streaming.serializer.DailyModelSerdes;
import com.capy.streaming.serializer.ReadingModelSerdes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReadingKafkaProcessor {

    @Autowired
    public void buildPipeLine(StreamsBuilder streamsBuilder) {
        streamsBuilder
                .stream("capy-broker", Consumed.with(Serdes.Long(), ReadingModelSerdes.serdes()))
                .map((timestamp, readingModel) -> KeyValue.pair(getDayTimestampFromTimestamp(timestamp), readingModel))
                .groupByKey(Grouped.with(Serdes.Long(), ReadingModelSerdes.serdes()))
                .aggregate(
                        DailyModel::new,
                        (aLong, readingModel, dailyModel) -> new DailyModel(
                                readingModel.sensorId(),
                                readingModel.sensorType(),
                                readingModel.label(),
                                Float.min(readingModel.value(), dailyModel.min),
                                Float.max(readingModel.value(), dailyModel.max),
                                getAvgValue(readingModel, dailyModel),
                                dailyModel.sum + readingModel.value(),
                                dailyModel.total + 1
                        ),
                        Materialized.with(Serdes.Long(), DailyModelSerdes.serdes())
                )
                .toStream()
                .peek((key, dailyModel) -> System.out.println("Produced dailymodel with key= " + key + ", dailyModel: " + dailyModel))
                .to("capy-daily", Produced.with(Serdes.Long(), DailyModelSerdes.serdes()));
    }

    private float getAvgValue(ReadingModel readingModel, DailyModel dailyModel) {
        return (dailyModel.sum + readingModel.value()) / (dailyModel.total + 1);
    }

    private Long getDayTimestampFromTimestamp(Long timestamp) {
        return (timestamp / 86400L) * 86400L;
    }
}
