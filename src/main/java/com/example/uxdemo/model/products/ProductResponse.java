package com.example.uxdemo.model.products;

import lombok.Data;

@Data
public class ProductResponse {

    private long id;
    private String idaccount;
    private String accounttype;
    private double balance;
    private String idclient;
    private String currency;
    private double comission;
    private int movementlimit;
    private int uniquedayofmovement;
}
