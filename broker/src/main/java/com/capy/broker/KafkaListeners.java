package com.capy.broker;

import com.capy.broker.objects.ReadingModel;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(
            topics = "capy-broker",
            groupId = "group-capy",
            containerFactory = "factory"
    )
    void listener(ConsumerRecord<Integer, ReadingModel> record) {
        //TODO Fill this appropriately
        String message = "This should be a message from " + record.value().label() +
                "Timestamp: " + record.value().timestamp() +
                "Value: " + record.value().value();
        System.out.println(message);
    }
}
