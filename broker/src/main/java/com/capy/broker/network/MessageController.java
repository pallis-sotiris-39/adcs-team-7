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

    private final KafkaTemplate<Long, ReadingModel> kafkaTemplate;

    public MessageController(KafkaTemplate<Long, ReadingModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void publish(@RequestBody ReadingRequest readingRequest) {
        kafkaTemplate.send(
                "capy-broker",
                readingRequest.timestamp(),
                new ReadingModel(
                        readingRequest.sensorId(),
                        readingRequest.sensorType(),
                        readingRequest.label(),
                        readingRequest.value()
                )
        );
    }
}
