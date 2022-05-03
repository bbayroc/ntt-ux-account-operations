package com.example.uxaccountoperations.model.yanki;

import lombok.Data;

@Data
public class YankiTransaction {

        private String sender;
        private String recipient;
        private double amount;
        private String created;

    }
