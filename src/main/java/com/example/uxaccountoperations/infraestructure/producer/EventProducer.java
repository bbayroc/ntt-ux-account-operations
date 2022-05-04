package com.example.uxaccountoperations.infraestructure.producer;

import com.example.uxaccountoperations.infraestructure.events.TransactionEvent;

public interface EventProducer {
    void produce(String topic, TransactionEvent event);
}
