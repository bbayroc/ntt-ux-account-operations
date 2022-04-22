package com.example.uxdemo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
