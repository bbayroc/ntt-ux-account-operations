package com.example.uxaccountoperations.model;

import lombok.Data;

@Data
public class TransactionEvent extends BaseEvent {

    private String idaccount;
    private String transactiontype;
    private double amount;
    private double appliedcomission;
    private BalanceUpdate balance;
}