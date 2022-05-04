package com.example.uxaccountoperations.controller;

import com.example.uxaccountoperations.business.ServiceCardList;
import com.example.uxaccountoperations.business.ServiceList;
import com.example.uxaccountoperations.business.ServiceTransfer;
import com.example.uxaccountoperations.model.BalanceResponse;
import com.example.uxaccountoperations.model.BalanceUpdate;
import com.example.uxaccountoperations.model.cards.CardResponse;
import com.example.uxaccountoperations.model.products.ProductRequest;
import com.example.uxaccountoperations.model.products.ProductResponse;
import com.example.uxaccountoperations.model.transactions.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class DemoController {

    @Autowired
    ServiceList service;

    @Autowired
    ServiceTransfer serviceTransfer;

    @Autowired
    ServiceCardList service2;

    @GetMapping("/balance/{dni}")
    public BalanceResponse getBalance(@PathVariable("dni") String dni, @RequestBody ProductRequest productRequest) throws IOException {

        BalanceResponse balanceResponse = new BalanceResponse();

        ProductResponse productResponse = service.validator(dni, productRequest.getIdaccount(), productRequest.getClienttype());

        balanceResponse.setBalance(productResponse.getBalance());

        balanceResponse.setCurrency(productResponse.getCurrency());

        return balanceResponse;
    }

    @GetMapping("/transaction/{idaccount}")
    public List<TransactionResponse> getTransaction(@PathVariable("idaccount") String idaccount, @RequestBody ProductRequest productRequest)
            throws IOException {

        ProductResponse productResponse = service.validator(productRequest.getIdclient(), idaccount, productRequest.getClienttype());

        return service.getTransaction(productResponse.getIdaccount());
    }

    @RequestMapping("/product/{idaccount}")
    public TransactionResponse postTransaction(@PathVariable("idaccount") String idaccount, @RequestBody ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = service.validator(productRequest.getIdclient(), idaccount, productRequest.getClienttype());

        return service.transactionValidator(productRequest, productResponse);
    }

    @RequestMapping("/transfer/{idaccount}")
    public TransactionResponse postTransfer(@PathVariable("idaccount") String idaccount, @RequestBody ProductRequest productRequest) throws IOException {

        return serviceTransfer.postTransfer(idaccount, productRequest);

    }

    @RequestMapping("/transfer/cards/{idaccount}")
    public TransactionResponse postTransfer2(@PathVariable("idaccount") String idaccount, @RequestBody ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = service.validator(productRequest.getIdclient(), idaccount, productRequest.getClienttype());
        CardResponse cardResponse = service2.cardValidator(productRequest.getAccounttransfer());

        BalanceUpdate balanceUpdate = new BalanceUpdate();
        balanceUpdate.setBalance(productRequest.getAmount());

        //Valida que la operacion se pueda realizar
        if (productResponse == null || (productRequest.getAmount()) < 0 || productResponse.getBalance() - productRequest.getAmount() < 0) {
            return null;
        }

        //Valida que se pueda transferir a la cuenta destino
        else if (cardResponse != null) {

            service2.updateCard(productRequest.getAccounttransfer(), balanceUpdate);
            service.postTransaction(productRequest, productRequest.getAccounttransfer());

            balanceUpdate.setBalance(-productRequest.getAmount());
            productRequest.setAmount(-productRequest.getAmount());

            service.updateProduct(idaccount, balanceUpdate);

            return service.postTransaction(productRequest, idaccount);

        }
        else {
            return null;
        }
    }
}