package com.example.uxdemo.controller;

import com.example.uxdemo.model.*;
import com.example.uxdemo.business.ServiceCardList;
import com.example.uxdemo.business.ServiceList;
import com.example.uxdemo.model.cards.Account;
import com.example.uxdemo.model.cards.CardRequest;
import com.example.uxdemo.model.cards.CardResponse;
import com.example.uxdemo.model.cards.DebitcardResponse;
import com.example.uxdemo.model.products.ProductRequest;
import com.example.uxdemo.model.products.ProductResponse;
import com.example.uxdemo.model.transactions.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cards")
public class DemoCardController {

    @Autowired
    ServiceCardList service;

    @Autowired
    ServiceList service2;

    @GetMapping("/balance/{dni}")
    public BalanceResponse getBalance(@PathVariable("dni") String dni, @RequestBody CardRequest cardRequest) throws IOException {

        BalanceResponse balanceResponse = new BalanceResponse();

        if (Objects.equals(cardRequest.getCardtype(), "Credit")) {

            CardResponse cardResponse = service.Validator(dni, cardRequest.getIdcard(), cardRequest.getClienttype());

            balanceResponse.setBalance(cardResponse.getBalance());

            balanceResponse.setCurrency(cardResponse.getCurrency());

        }
        else if (Objects.equals(cardRequest.getCardtype(), "Debit")) {

            DebitcardResponse debitcardResponse = service.getdebitcard(cardRequest.getIdcard());

            //ProductResponse productResponse = service2.Validator(dni, debitcardResponse.getPrincipalaccount(), cardRequest.getClienttype());

            ProductResponse productResponse = service2.accountValidator(debitcardResponse.getPrincipalaccount());

            balanceResponse.setBalance(productResponse.getBalance());

            balanceResponse.setCurrency(productResponse.getCurrency());

        }

        return balanceResponse;
    }

    @GetMapping("/debit/{idcard}")
    public DebitcardResponse getDebitCard(@PathVariable("idcard") String idcard) throws IOException {

        return service.getdebitcard(idcard);
    }

    @PatchMapping("/debit/{idcard}")
    public DebitcardResponse UpdateDebitCard(@PathVariable String idcard, @RequestBody Account account) throws IOException {

        return service.updatedebitcard(idcard, account);
    }

    @GetMapping("/transaction/{idcard}")
    public List<TransactionResponse> getTransaction(@PathVariable("idcard") String idcard, @RequestBody CardRequest cardRequest) throws IOException {

        CardResponse cardResponse = service.Validator(cardRequest.getIdclient(), idcard, cardRequest.getClienttype());

        return service.getTransaction(cardResponse.getIdcard());
    }

    @RequestMapping("/card/{idcard}")
    public TransactionResponse postTransaction(@PathVariable("idcard") String idcard, @RequestBody CardRequest cardRequest) throws IOException {

        CardResponse cardResponse = service.Validator(cardRequest.getIdclient(), idcard, cardRequest.getClienttype());

        BalanceUpdate balanceUpdate = new BalanceUpdate();

        //Valida la cuenta y que los retiros sean numeros negativos
        if (cardResponse == null || (Objects.equals(cardRequest.getTransactiontype(), "Consumo") && cardRequest.getAmount() >= 0) || (Objects.equals(cardRequest.getTransactiontype(), "Pago") && cardRequest.getAmount() <= 0)) {
            return null;
        }
        else
            //Valida que el retiro no sea mayor al balance
            if ((cardResponse.getBalance() + cardRequest.getAmount()) < 0) {
                return null;
            }
            else {
                balanceUpdate.setBalance(cardRequest.getAmount());
            }

        service.updateCard(idcard, balanceUpdate);

        return service.postTransaction(cardRequest, idcard);
    }

    @RequestMapping("/debitCard/{idcard}")
    public TransactionResponse postDebitTransaction(@PathVariable("idcard") String idcard, @RequestBody ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = service2.Validator(productRequest.getIdclient(), productRequest.getIdaccount(), productRequest.getClienttype());

        TransactionResponse transactionResponse = service2.transactionValidator(productRequest, productResponse);

        if (transactionResponse == null) {

            DebitcardResponse debitcardResponse = service.getdebitcard(idcard);

            List<Account> accounts = debitcardResponse.getAccount().stream().sorted(Comparator.comparing(Account::getAdded)).collect(Collectors.toList());

            for (int i = 0; i < accounts.size(); i++) {
                productResponse = service2.accountValidator(accounts.get(i).getIdaccount());
                transactionResponse = service2.transactionValidator(productRequest, productResponse);
                if (transactionResponse != null) {
                    break;
                }
            }
        }

        return transactionResponse;
    }

}