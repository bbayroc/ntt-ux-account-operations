package com.example.uxaccountoperations.infraestructure.events;

public interface EventHandler {
    void on(TransactionEvent event);
}
