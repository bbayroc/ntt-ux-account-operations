package com.example.uxdemo.entity;

import lombok.Data;

import java.util.List;

@Data
public class DebitcardResponse {

    private long id;
    private String idcard;
    private String cardtype;
    private String idclient;
    private String created;
    private String clienttype;
    private String principalaccount;
    private String accounttype;
    private List<Account> account;

}
