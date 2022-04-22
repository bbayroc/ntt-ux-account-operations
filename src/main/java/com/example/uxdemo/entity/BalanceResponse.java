package com.example.uxdemo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@Data
public class BalanceResponse {

    private double balance;
    private String currency;

}
