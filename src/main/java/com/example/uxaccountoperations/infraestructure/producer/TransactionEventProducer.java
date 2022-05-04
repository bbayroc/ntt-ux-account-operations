package com.example.uxaccountoperations.infraestructure.producer;

import com.example.uxaccountoperations.infraestructure.events.BaseEvent;
import com.example.uxaccountoperations.infraestructure.events.TransactionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionEventProducer implements EventProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void produce(String topic, TransactionEvent event) {
        this.kafkaTemplate.send(topic, event);
    }
}
