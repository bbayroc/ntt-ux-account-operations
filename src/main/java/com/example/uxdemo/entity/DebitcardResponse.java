package com.example.uxdemo.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class DebitcardResponse {

    private long id;
    private String idcard;
    private String cardtype;
    private String idclient;
    private Date created;
    private String clienttype;
    private List<Account> account;

}
