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
import java.util.Set;

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


            //Valida la cuenta y que los retiros sean numeros negativos
            if (productResponse == null || (Objects.equals(productRequest.getTransactiontype(), "Retiro") && productRequest.getAmount() >= 0) || (Objects.equals(productRequest.getTransactiontype(), "Deposito") && productRequest.getAmount() <= 0))
                return null;
            else
                //Valida que el retiro no sea mayor al balance
                if ((productResponse.getBalance() + productRequest.getAmount()) < 0 || (service.limitValidator(idaccount) && !Set.of("PYME", "VIP").contains(productResponse.getAccounttype()))) {
                    return null;
                } else

                    balanceUpdate.setBalance(productRequest.getAmount());

            if (!Objects.equals(productResponse.getAccounttype(), "Cuenta Corriente") && service.limitValidator(idaccount))    {

                balanceUpdate.setBalance(productRequest.getAmount() - productResponse.getComission());
                productRequest.setAppliedcomision(-productResponse.getComission());
            }

        service.updateProduct(idaccount, balanceUpdate);

        return service.postTransaction(productRequest, idaccount);
    }

    @RequestMapping("/Transfer/{idaccount}")
    public TransactionResponse postTransfer(@PathVariable("idaccount") String idaccount, @RequestBody ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = service.Validator(productRequest.getIdclient(), idaccount, productRequest.getClienttype());
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
}