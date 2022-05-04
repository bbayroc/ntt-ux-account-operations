package com.example.uxaccountoperations.infraestructure.events;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class TransactionEvent extends BaseEvent {
    private String idaccount;
    private String transactiontype;
    private double amount;
    private double appliedcomission;
    private BalanceUpdate balance;
}
