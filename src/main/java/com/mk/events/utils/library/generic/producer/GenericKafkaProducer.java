package com.mk.events.utils.library.generic.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class GenericKafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public GenericKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Message sent: " + message);
    }
}
