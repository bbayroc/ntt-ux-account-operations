package com.example.uxdemo.entity;

import lombok.Data;
@Data
public class CardResponse {

    private long id;
    private String idcard;
    private String cardtype;
    private double balance;
    private String idclient;
    private String clienttype;
    private String currency;
}
