package com.example.uxdemo.model.cards;

import lombok.Data;

@Data
public class CardRequest {

    private String idcard;
    private String cardtype;
    private double balance;
    private String idclient;
    private String clienttype;
    private String currency;
    private String transactiontype;
    private double amount;
    private double appliedcomision;
    private String idaccount;
}
