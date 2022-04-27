package com.example.uxdemo.controller;

import com.example.uxdemo.entity.*;
import com.example.uxdemo.util.ServiceCardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/Cards")
public class DemoCardController {

    @Autowired
    ServiceCardList service;

    @GetMapping("/Balance/{dni}")
    public BalanceResponse getBalance(@PathVariable("dni") String dni, @RequestBody CardRequest cardRequest) throws IOException {

        BalanceResponse balanceResponse = new BalanceResponse();

        CardResponse cardResponse = service.Validator(dni, cardRequest.getIdcard(), cardRequest.getClienttype());

        balanceResponse.setBalance(cardResponse.getBalance());

        balanceResponse.setCurrency(cardResponse.getCurrency());

        return balanceResponse;
    }

    @GetMapping("/Debit/{idcard}")
    public DebitcardResponse getDebitCard(@PathVariable("idcard") String idcard) throws IOException {

        return service.getdebitcard(idcard);
    }

    @PatchMapping("/Debit/{idcard}")
    public DebitcardResponse UpdateDebitCard(@PathVariable String idcard, @RequestBody Account account) throws IOException {

        return service.updatedebitcard(idcard, account);
    }

    @GetMapping("/Transaction/{idcard}")
    public List<TransactionResponse> getTransaction(@PathVariable("idcard") String idcard, @RequestBody CardRequest cardRequest) throws IOException {

        CardResponse cardResponse = service.Validator(cardRequest.getIdclient(), idcard, cardRequest.getClienttype());

        return service.getTransaction(cardResponse.getIdcard());
    }

    @RequestMapping("/Card/{idcard}")
    public TransactionResponse postTransaction(@PathVariable("idcard") String idcard, @RequestBody CardRequest cardRequest) throws IOException {

        CardResponse cardResponse = service.Validator(cardRequest.getIdclient(), idcard, cardRequest.getClienttype());

        BalanceUpdate balanceUpdate = new BalanceUpdate();

            //Valida la cuenta y que los retiros sean numeros negativos
            if (cardResponse == null || (Objects.equals(cardRequest.getTransactiontype(), "Consumo") && cardRequest.getAmount() >= 0) || (Objects.equals(cardRequest.getTransactiontype(), "Pago") && cardRequest.getAmount() <= 0))
                return null;
            else
                //Valida que el retiro no sea mayor al balance
                if ((cardResponse.getBalance() + cardRequest.getAmount()) < 0) {
                    return null;
                } else

                    balanceUpdate.setBalance(cardRequest.getAmount());

        service.updateCard(idcard, balanceUpdate);

        return service.postTransaction(cardRequest, idcard);
    }

}