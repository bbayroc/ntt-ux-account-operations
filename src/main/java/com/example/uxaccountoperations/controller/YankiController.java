package com.example.uxaccountoperations.controller;

import com.example.uxaccountoperations.business.*;
import com.example.uxaccountoperations.model.BalanceResponse;
import com.example.uxaccountoperations.model.cards.CardResponse;
import com.example.uxaccountoperations.model.cards.DebitcardResponse;
import com.example.uxaccountoperations.model.products.ProductRequest;
import com.example.uxaccountoperations.model.products.ProductResponse;
import com.example.uxaccountoperations.model.transactions.TransactionResponse;
import com.example.uxaccountoperations.model.yanki.YankiRequest;
import com.example.uxaccountoperations.model.yanki.YankiResponse;
import com.example.uxaccountoperations.model.yanki.YankiTransaction;
import com.example.uxaccountoperations.web.CardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.Call;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/yanki")
public class YankiController {

    @Autowired
    ServiceYankiList service;

    @Autowired
    ServiceTransfer serviceTransfer;

    @Autowired
    ServiceList servicelist;

    @Autowired
    CardsService cardservice;

    @Autowired
    ServiceKafka serviceKafka;

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

        if (sender.getDebitcard() != null && recipient.getDebitcard() == null) {

            Call<DebitcardResponse> call1 = cardservice.debitcardrequest(sender.getDebitcard());

            DebitcardResponse debitcardResponse = call1.execute().body();

            ProductResponse productResponse = servicelist.validator(debitcardResponse.getIdclient(), debitcardResponse.getPrincipalaccount(), debitcardResponse.getClienttype());

            ProductRequest productRequest = new ProductRequest();

            productRequest.setAccounttype(productResponse.getAccounttype());
            productRequest.setClienttype(debitcardResponse.getClienttype());
            productRequest.setIdclient(productResponse.getIdclient());
            productRequest.setTransactiontype("Retiro");
            productRequest.setAmount(-yankiTransaction.getAmount());

            serviceKafka.transactionValidator(productRequest, productResponse);

            return service.transaction(cellphone, yankiTransaction);

        }

        else if (sender.getDebitcard() == null && recipient.getDebitcard() != null) {

            Call<DebitcardResponse> call2 = cardservice.debitcardrequest(recipient.getDebitcard());

            DebitcardResponse debitcardResponse = call2.execute().body();

            ProductResponse productResponse = servicelist.validator(debitcardResponse.getIdclient(), debitcardResponse.getPrincipalaccount(), debitcardResponse.getClienttype());

            ProductRequest productRequest = new ProductRequest();

            productRequest.setAccounttype(productResponse.getAccounttype());
            productRequest.setClienttype(debitcardResponse.getClienttype());
            productRequest.setIdclient(productResponse.getIdclient());
            productRequest.setTransactiontype("Deposito");
            productRequest.setAmount(yankiTransaction.getAmount());

            serviceKafka.transactionValidator(productRequest, productResponse);

            return service.transaction(cellphone, yankiTransaction);

        }

        else if (sender.getDebitcard() != null && recipient.getDebitcard() != null) {

            Call<DebitcardResponse> call3 = cardservice.debitcardrequest(sender.getDebitcard());

            DebitcardResponse debitcardSender = call3.execute().body();

            ProductResponse productSender = servicelist.validator(debitcardSender.getIdclient(), debitcardSender.getPrincipalaccount(), debitcardSender.getClienttype());

            ProductRequest productRequest = new ProductRequest();

            productRequest.setAccounttype(productSender.getAccounttype());
            productRequest.setClienttype(debitcardSender.getClienttype());
            productRequest.setIdclient(productSender.getIdclient());
            productRequest.setTransactiontype("Retiro");
            productRequest.setAmount(-yankiTransaction.getAmount());

            serviceKafka.transactionValidator(productRequest, productSender);

            Call<DebitcardResponse> call4 = cardservice.debitcardrequest(recipient.getDebitcard());

            DebitcardResponse debitcardRecipient = call4.execute().body();

            ProductResponse productRecipient = servicelist.validator(debitcardRecipient.getIdclient(), debitcardRecipient.getPrincipalaccount(), debitcardRecipient.getClienttype());

            ProductRequest productRequest2 = new ProductRequest();

            productRequest2.setAccounttype(productRecipient.getAccounttype());
            productRequest2.setClienttype(debitcardRecipient.getClienttype());
            productRequest2.setIdclient(productRecipient.getIdclient());
            productRequest2.setTransactiontype("Deposito");
            productRequest2.setAmount(yankiTransaction.getAmount());

            serviceKafka.transactionValidator(productRequest2, productRecipient);

            return service.transaction(cellphone, yankiTransaction);

/*
            Call<DebitcardResponse> call4 = cardservice.debitcardrequest(sender.getDebitcard());

            System.out.println(recipient.getDebitcard());

            DebitcardResponse debitcardSender = call4.execute().body();

            Call<DebitcardResponse> call3 = cardservice.debitcardrequest(recipient.getDebitcard());

            System.out.println(recipient.getDebitcard());

            DebitcardResponse debitcardRecipient = call3.execute().body();

            ProductRequest productRequest = new ProductRequest();

            productRequest.setAmount(yankiTransaction.getAmount());
            productRequest.setAccounttransfer(debitcardRecipient.getPrincipalaccount());
            productRequest.setIdclient(debitcardSender.getIdclient());
            productRequest.setClienttype(debitcardSender.getClienttype());

            serviceTransfer.postTransfer(debitcardSender.getPrincipalaccount(), productRequest);

            return service.transaction(cellphone, yankiTransaction);
*/
        }

        else {

            YankiTransaction yankiTransaction2 = service.transaction(cellphone, yankiTransaction);

            return yankiTransaction2;
        }

    }

    }

