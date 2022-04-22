package com.example.uxdemo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
}
