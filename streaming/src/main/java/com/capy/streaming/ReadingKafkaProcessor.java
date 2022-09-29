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
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReadingKafkaProcessor {

    @Autowired
    public void buildPipeLine(StreamsBuilder streamsBuilder) {

        KStream<Long, ReadingModel> kStream = streamsBuilder
                .stream("capy-broker", Consumed.with(Serdes.Long(), ReadingModelSerdes.serdes()));
        kStream
                .filter((timestamp, readingModel) -> readingModel.isLate())
                .to("capy-late", Produced.with(Serdes.Long(), ReadingModelSerdes.serdes()));

        Map<String, KStream<Long, ReadingModel>> streams = kStream
                .map((timestamp, readingModel) -> KeyValue.pair(getDayTimestampFromTimestamp(timestamp), readingModel))
                .split(Named.as("branch-"))
                .branch((timestamp, readingModel) -> readingModel.sensorId() == 2, Branched.as("2"))
                .branch((timestamp, readingModel) -> readingModel.sensorId() == 1, Branched.as("1"))
                .defaultBranch(Branched.as("0"));

        streams.forEach((mapKey, stream) ->
                stream
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
                        .to("capy-daily", Produced.with(Serdes.Long(), DailyModelSerdes.serdes()))
        );
    }

    private float getAvgValue(ReadingModel readingModel, DailyModel dailyModel) {
        return (dailyModel.sum + readingModel.value()) / (dailyModel.total + 1);
    }

    private Long getDayTimestampFromTimestamp(Long timestamp) {
        return (timestamp / 86400L) * 86400L;
    }
}
