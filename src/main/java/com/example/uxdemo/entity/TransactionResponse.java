package com.example.uxdemo.entity;

import lombok.Data;
@Data
public class TransactionResponse {

    private long id;
    private String idaccount;
    private String transactiontype;
    private double amount;
    private String created;
    private double appliedcomission;

}
