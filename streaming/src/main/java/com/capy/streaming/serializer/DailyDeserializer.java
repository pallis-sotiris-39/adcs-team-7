package com.capy.streaming.serializer;

import com.capy.streaming.objects.DailyModel;
import com.capy.streaming.objects.ReadingModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class DailyDeserializer implements Deserializer<DailyModel> {

    public DailyDeserializer() {}

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public DailyModel deserialize(String topic, Headers headers, byte[] data) {
        try {
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), DailyModel.class);
        } catch (Exception e) {
            System.err.println("Exception!" + e + "Unable to serialize model: " + Arrays.toString(data));
            return null;
        }
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }

    @Override
    public DailyModel deserialize(String s, byte[] bytes) {
        try {
            return objectMapper.readValue(new String(bytes, StandardCharsets.UTF_8), DailyModel.class);
        } catch (Exception e) {
            System.err.println("Exception!" + e + "Unable to serialize model: " + Arrays.toString(bytes));
            return null;
        }
    }
}
