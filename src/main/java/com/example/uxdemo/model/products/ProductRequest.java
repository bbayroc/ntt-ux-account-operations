package com.example.uxdemo.model.products;

import lombok.Data;

@Data
public class ProductRequest {

    private String idaccount;
    private String accounttype;
    private String clienttype;
    private String idclient;
    private String transactiontype;
    private double balance;
    private double comission;
    private int movementlimit;
    private int uniquedayofmovement;
    private double amount;
    private double appliedcomision;
    private String accounttransfer;
    private String idcard;
    private String cardtype;
}
