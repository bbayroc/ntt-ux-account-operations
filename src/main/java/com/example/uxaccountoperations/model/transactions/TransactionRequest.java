package com.example.uxaccountoperations.model.transactions;

import lombok.Data;
@Data
public class TransactionRequest {

    private String idaccount;
    private String transactiontype;
    private double amount;
    private double appliedcomission;
}