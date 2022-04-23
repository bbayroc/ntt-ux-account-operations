package com.example.uxdemo.controller;

import com.example.uxdemo.entity.*;
import com.example.uxdemo.util.ServiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@RestController
public class DemoController {


    @Autowired
    ServiceList service;

    @GetMapping("/Balance/{dni}")
    public BalanceResponse getBalance(@PathVariable("dni") String dni, @RequestBody ProductRequest productRequest) throws IOException {

        BalanceResponse balanceResponse = new BalanceResponse();

        ProductResponse productResponse = service.Validator(dni, productRequest.getIdaccount(), productRequest.getClienttype());

        balanceResponse.setBalance(productResponse.getBalance());

        balanceResponse.setCurrency(productResponse.getCurrency());

        return balanceResponse;
    }

    @GetMapping("/Transaction/{idaccount}")
    public List<TransactionResponse> getTransaction(@PathVariable("idaccount") String idaccount, @RequestBody ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = service.Validator(productRequest.getIdclient(), idaccount, productRequest.getClienttype());

        return service.getTransaction(productResponse.getIdaccount());
    }

    @RequestMapping("/Product/{idaccount}")
    public TransactionResponse postTransaction(@PathVariable("idaccount") String idaccount, @RequestBody ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = service.Validator(productRequest.getIdclient(), idaccount, productRequest.getClienttype());

        BalanceUpdate balanceUpdate = new BalanceUpdate();

        if (productResponse.getMovementlimit() > 0 && (productResponse.getUniquedayofmovement() == 0) || productResponse.getUniquedayofmovement() == LocalDateTime.now().getDayOfMonth()) {

            List<TransactionResponse> transactions = service.getTransaction(productResponse.getIdaccount());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            long count = transactions.stream()
                    .map(c -> LocalDateTime.parse(c.getCreated(), formatter).toLocalDate().getMonth())
                    .filter((c) -> c.equals(LocalDateTime.now().getMonth()))
                    .count();
            if (count >= productResponse.getMovementlimit())
                return null;
        } else
            //Valida la cuenta y que los retiros sean numeros negativos
            if (productResponse == null || (Objects.equals(productRequest.getTransactiontype(), "Retiro") && productRequest.getAmount() >= 0) || (Objects.equals(productRequest.getTransactiontype(), "Deposito") && productRequest.getAmount() <= 0))
                return null;
            else
                //Valida que el retiro no sea mayor al balance
                if ((productResponse.getBalance() + productRequest.getAmount()) < 0) {
                    return null;
                } else

                    balanceUpdate.setBalance(productRequest.getAmount());

        service.updateProduct(idaccount, balanceUpdate);

        return service.postTransaction(productRequest, idaccount);
    }

    @RequestMapping("/Transfer/{idaccount}")
    public TransactionResponse postTransfer(@PathVariable("idaccount") String idaccount, @RequestBody ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = service.Validator(productRequest.getIdclient(), idaccount, productRequest.getClienttype());

        BalanceUpdate balanceUpdate = new BalanceUpdate();
        balanceUpdate.setBalance(productRequest.getAmount());

            //Valida que la operacion se pueda realizar
            if (productResponse == null || (productRequest.getAmount()) < 0 || productResponse.getBalance() - productRequest.getAmount() < 0 || service.limitValidator(idaccount) || service.limitValidator(productRequest.getAccounttransfer()))
                return null;

                //Valida que se pueda transferir a la cuenta destino
            else if (service.updateProduct(productRequest.getAccounttransfer(), balanceUpdate) != null) {

                service.postTransaction(productRequest, productRequest.getAccounttransfer());

                balanceUpdate.setBalance(-1 * productRequest.getAmount());
                productRequest.setAmount(-productRequest.getAmount());

                service.updateProduct(idaccount, balanceUpdate);

                return service.postTransaction(productRequest, idaccount);

            } else return null;


    }
}