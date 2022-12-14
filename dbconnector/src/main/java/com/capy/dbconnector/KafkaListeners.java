package com.capy.dbconnector;

import com.capy.dbconnector.objects.DailyModel;
import com.capy.dbconnector.objects.ReadingModel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @Autowired
    InfluxDBClient influxClient;

    @KafkaListener(
            topics = "capy-daily",
            groupId = "group-daily",
            containerFactory = "dailyFactory"
    )
    void listener(ConsumerRecord<Long, DailyModel> record) {
        try {
            WriteApiBlocking writeApi = influxClient.getWriteApiBlocking();
            Point point = Point
                    .measurement("sensor")
                    .addTag("sensor_id", String.valueOf(record.value().sensorId))
                    .addTag("label", record.value().label)
                    .addField("sensor_type", record.value().sensorType.toString())
                    .addField("min", record.value().min)
                    .addField("max", record.value().max)
                    .addField("avg", record.value().avg)
                    .addField("sum", record.value().sum)
                    .time(record.key(), WritePrecision.S);
            writeApi.writePoint(point);
        } catch (InfluxException e) {
            System.out.println("Exception!!" + e.getMessage());
        }
    }

    @KafkaListener(
            topics = "capy-late",
            groupId = "group-daily",
            containerFactory = "lateFactory"
    )
    void lateListener(ConsumerRecord<Long, ReadingModel> record) {
        try {
            WriteApiBlocking writeApi = influxClient.getWriteApiBlocking();
            Point point = Point
                    .measurement("sensor-late")
                    .addTag("sensor_id", String.valueOf(record.value().sensorId()))
                    .addTag("label", record.value().label())
                    .addField("sensor_type", record.value().sensorType().toString())
                    .addField("value", record.value().value())
                    .time(record.key(), WritePrecision.S);
            writeApi.writePoint(point);
        } catch (InfluxException e) {
            System.out.println("Exception!!" + e.getMessage());
        }
    }
}
