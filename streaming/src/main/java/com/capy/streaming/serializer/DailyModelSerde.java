package com.capy.streaming.serializer;

import com.capy.streaming.objects.DailyModel;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class DailyModelSerde implements Serde<DailyModel> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serde.super.configure(configs, isKey);
    }

    @Override
    public void close() {
        Serde.super.close();
    }

    @Override
    public Serializer<DailyModel> serializer() {
        return new DailySerializer();
    }

    @Override
    public Deserializer<DailyModel> deserializer() {
        return new DailyDeserializer();
    }
}
