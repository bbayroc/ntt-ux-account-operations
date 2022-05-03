package com.example.uxaccountoperations.model.yanki;

import lombok.Data;

@Data
public class YankiResponse {

    private long id;
    private String identification;
    private String cellphone;
    private String imei;
    private String email;
    private String debitcard;
    private double balance;
    private String currency;

}
