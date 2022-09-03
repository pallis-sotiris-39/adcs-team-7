package com.capy.broker.network;

import com.capy.broker.objects.ReadingModel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/readings")
public class MessageController {

    private final KafkaTemplate<Integer, ReadingModel> kafkaTemplate;

    public MessageController(KafkaTemplate<Integer, ReadingModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void publish(@RequestBody ReadingRequest readingRequest) {
        kafkaTemplate.send(
                "capy-broker",
                readingRequest.sensorId(),
                new ReadingModel(
                        readingRequest.sensorType(),
                        readingRequest.label(),
                        readingRequest.timestamp(),
                        readingRequest.value()
                )
        );
    }
}
