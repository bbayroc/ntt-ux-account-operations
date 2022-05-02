package com.example.uxdemo.controller;

import com.example.uxdemo.business.ServiceYankiList;
import com.example.uxdemo.model.BalanceResponse;
import com.example.uxdemo.model.products.ProductRequest;
import com.example.uxdemo.model.products.ProductResponse;
import com.example.uxdemo.model.yanki.YankiRequest;
import com.example.uxdemo.model.yanki.YankiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/yanki")
public class YankiController {

    @Autowired
    ServiceYankiList service;

    @GetMapping("/balance/{identification}")
    public BalanceResponse getBalance(@PathVariable("identification") String identification, @RequestBody String cellphone) throws IOException {

        BalanceResponse balanceResponse = new BalanceResponse();

        YankiResponse yankiResponse = service.validator(identification);

        if (Objects.equals(cellphone, yankiResponse.getCellphone())) {

            balanceResponse.setBalance(yankiResponse.getBalance());

            balanceResponse.setCurrency(yankiResponse.getCurrency());

            return balanceResponse;
        } else return null;
    }

    @PatchMapping("/update/{identification}")
    public YankiResponse patch(@PathVariable String identification, @RequestBody YankiRequest yankirequest) throws IOException {

        YankiResponse yankiResponse = service.validator(identification);

        if (Objects.equals(null, yankiResponse.getDebitcard())) {

            return service.update(identification, yankirequest);

        } else return null;

    }
}