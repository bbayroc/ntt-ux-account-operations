package com.example.uxdemo.entity;

import lombok.Data;

@Data
public class YankiResponse {

    private long id;
    private String identification;
    private String imei;
    private String email;
    private String debitcard;
}
