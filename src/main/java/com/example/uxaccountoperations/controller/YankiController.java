package com.example.uxaccountoperations.controller;

import com.example.uxaccountoperations.business.ServiceYankiList;
import com.example.uxaccountoperations.model.BalanceResponse;
import com.example.uxaccountoperations.model.yanki.YankiRequest;
import com.example.uxaccountoperations.model.yanki.YankiResponse;
import com.example.uxaccountoperations.model.yanki.YankiTransaction;
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

    @RequestMapping("/transaction/{cellphone}")
    public YankiTransaction transaction(@PathVariable String cellphone, @RequestBody YankiTransaction yankiTransaction) throws IOException {

        YankiResponse sender = service.validatorcellphone(cellphone);
        YankiResponse recipient = service.validatorcellphone(yankiTransaction.getRecipient());

        if (sender.getDebitcard() == null || recipient.getDebitcard() == null) {

            return null;

        } else {
            YankiTransaction yankiTransaction2 = service.transaction(cellphone, yankiTransaction);

            return yankiTransaction2;
        }

    }
    @GetMapping("/transaction2/{cellphone}")
    public YankiResponse transaction2(@PathVariable String cellphone) throws IOException {

        YankiResponse yankiResponse = service.validatorcellphone(cellphone);

        return yankiResponse;
    }

    }

