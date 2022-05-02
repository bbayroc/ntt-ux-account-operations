package com.example.uxdemo.controller;

import com.example.uxdemo.entity.*;
import com.example.uxdemo.util.ServiceCardList;
import com.example.uxdemo.util.ServiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
public class DemoController {
    @Autowired
    ServiceList service;

    @Autowired
    ServiceCardList service2;
    @GetMapping("/Balance/{dni}")
    public BalanceResponse getBalance(@PathVariable("dni") String dni, @RequestBody ProductRequest productRequest) throws IOException {

        BalanceResponse balanceResponse = new BalanceResponse();

        ProductResponse productResponse = service.validator(dni, productRequest.getIdaccount(), productRequest.getClienttype());

        balanceResponse.setBalance(productResponse.getBalance());

        balanceResponse.setCurrency(productResponse.getCurrency());

        return balanceResponse;
    }

    @GetMapping("/Transaction/{idaccount}")
    public List<TransactionResponse> getTransaction(@PathVariable("idaccount") String idaccount, @RequestBody ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = service.validator(productRequest.getIdclient(), idaccount, productRequest.getClienttype());

        return service.getTransaction(productResponse.getIdaccount());
    }

    @RequestMapping("/Product/{idaccount}")
    public TransactionResponse postTransaction(@PathVariable("idaccount") String idaccount, @RequestBody ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = service.validator(productRequest.getIdclient(), idaccount, productRequest.getClienttype());

        return service.transactionValidator(productRequest, productResponse);
    }

    @RequestMapping("/Transfer/{idaccount}")
    public TransactionResponse postTransfer(@PathVariable("idaccount") String idaccount, @RequestBody ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = service.validator(productRequest.getIdclient(), idaccount, productRequest.getClienttype());
        ProductResponse productResponse2 = service.accountValidator(productRequest.getAccounttransfer());

        BalanceUpdate balanceUpdate = new BalanceUpdate();
        balanceUpdate.setBalance(productRequest.getAmount());

        BalanceUpdate balanceUpdate2 = new BalanceUpdate();
        balanceUpdate2.setBalance(productRequest.getAmount());

            //Valida que la operacion se pueda realizar
            if (productResponse == null || (productRequest.getAmount()) < 0 || productResponse.getBalance() - productRequest.getAmount() < 0 ||
                    (service.limitValidator(idaccount) && !Set.of("PYME", "VIP").contains(productResponse.getAccounttype())) ||
                    (service.limitValidator(productRequest.getAccounttransfer()) && !Set.of("PYME", "VIP").contains(productResponse2.getAccounttype())))
                return null;

                //Valida que se pueda transferir a la cuenta destino
            else if (productResponse2!= null) {

                if (Set.of("PYME", "VIP").contains(productResponse2.getAccounttype()) && service.limitValidator(productResponse2.getIdaccount()))    {

                    balanceUpdate2.setBalance(productRequest.getAmount() - productResponse2.getComission());
                    productRequest.setAppliedcomision(-productResponse2.getComission());
                }

                service.updateProduct(productRequest.getAccounttransfer(), balanceUpdate2);
                service.postTransaction(productRequest, productRequest.getAccounttransfer());

                balanceUpdate.setBalance(-productRequest.getAmount());
                productRequest.setAmount(-productRequest.getAmount());
                productRequest.setAppliedcomision(0.0);

                if (Set.of("PYME", "VIP").contains(productResponse.getAccounttype()) && service.limitValidator(productResponse.getIdaccount()))    {

                    balanceUpdate.setBalance(productRequest.getAmount() - productResponse.getComission());
                    productRequest.setAppliedcomision(-productResponse.getComission());
                }

                service.updateProduct(idaccount, balanceUpdate);

                return service.postTransaction(productRequest, idaccount);

            } else return null;
    }

    @RequestMapping("/Transfer/Cards/{idaccount}")
    public TransactionResponse postTransfer2(@PathVariable("idaccount") String idaccount, @RequestBody ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = service.validator(productRequest.getIdclient(), idaccount, productRequest.getClienttype());
        CardResponse cardResponse = service2.cardValidator(productRequest.getAccounttransfer());

        BalanceUpdate balanceUpdate = new BalanceUpdate();
        balanceUpdate.setBalance(productRequest.getAmount());

        //Valida que la operacion se pueda realizar
        if (productResponse == null || (productRequest.getAmount()) < 0 || productResponse.getBalance() - productRequest.getAmount() < 0)
            return null;

            //Valida que se pueda transferir a la cuenta destino
        else if (cardResponse != null) {

            service2.updateCard(productRequest.getAccounttransfer(), balanceUpdate);
            service.postTransaction(productRequest, productRequest.getAccounttransfer());

            balanceUpdate.setBalance(-productRequest.getAmount());
            productRequest.setAmount(-productRequest.getAmount());

            service.updateProduct(idaccount, balanceUpdate);

            return service.postTransaction(productRequest, idaccount);

        } else return null;
    }

    @GetMapping("/Yanki/Cellphone/{cellphone}")
    public YankiResponse getyanki(@PathVariable("cellphone") String cellphone) throws IOException {

        return service.yankiValidator(cellphone);
    }

    @RequestMapping("/Yanki/{cellphone}")
    public TransactionResponse getyankitransaction(@PathVariable("cellphone") String cellphone, @RequestBody ProductRequest productRequest) throws IOException {

        YankiResponse yankiResponse = service.yankiValidator(cellphone);
        DebitcardResponse debitcardResponse = service2.getdebitcard(yankiResponse.getDebitcard());
        ProductResponse productResponse = service.accountValidator(debitcardResponse.getPrincipalaccount());

        return service.transactionValidator(productRequest, productResponse);

    }

}