package com.example.uxdemo.model;

import lombok.Data;

@Data
public class YankiResponse {

    private long id;
    private String identification;
    private String imei;
    private String email;
    private String debitcard;
}
