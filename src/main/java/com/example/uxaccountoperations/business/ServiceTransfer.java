package com.example.uxaccountoperations.business;

import com.example.uxaccountoperations.model.BalanceUpdate;
import com.example.uxaccountoperations.model.products.ProductRequest;
import com.example.uxaccountoperations.model.products.ProductResponse;
import com.example.uxaccountoperations.model.transactions.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public class ServiceTransfer {

    @Autowired
    ServiceList service;

    public TransactionResponse postTransfer(String idaccount, ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = service.validator(productRequest.getIdclient(), idaccount, productRequest.getClienttype());
        ProductResponse productResponse2 = service.accountValidator(productRequest.getAccounttransfer());

        BalanceUpdate balanceUpdate = new BalanceUpdate();
        balanceUpdate.setBalance(productRequest.getAmount());

        BalanceUpdate balanceUpdate2 = new BalanceUpdate();
        balanceUpdate2.setBalance(productRequest.getAmount());

        //Valida que la operacion se pueda realizar
        if (productResponse == null || (productRequest.getAmount()) < 0 || productResponse.getBalance() - productRequest.getAmount() < 0 || (service.limitValidator(idaccount) && !Set.of("PYME", "VIP").contains(productResponse.getAccounttype())) || (service.limitValidator(productRequest.getAccounttransfer()) && !Set.of("PYME", "VIP").contains(productResponse2.getAccounttype()))) {
            return null;
        }

        //Valida que se pueda transferir a la cuenta destino
        else if (productResponse2 != null) {

            if (Set.of("PYME", "VIP").contains(productResponse2.getAccounttype()) && service.limitValidator(productResponse2.getIdaccount())) {

                balanceUpdate2.setBalance(productRequest.getAmount() - productResponse2.getComission());
                productRequest.setAppliedcomision(-productResponse2.getComission());
            }

            service.updateProduct(productRequest.getAccounttransfer(), balanceUpdate2);
            service.postTransaction(productRequest, productRequest.getAccounttransfer());

            balanceUpdate.setBalance(-productRequest.getAmount());
            productRequest.setAmount(-productRequest.getAmount());
            productRequest.setAppliedcomision(0.0);

            if (Set.of("PYME", "VIP").contains(productResponse.getAccounttype()) && service.limitValidator(productResponse.getIdaccount())) {

                balanceUpdate.setBalance(productRequest.getAmount() - productResponse.getComission());
                productRequest.setAppliedcomision(-productResponse.getComission());
            }

            service.updateProduct(idaccount, balanceUpdate);

            return service.postTransaction(productRequest, idaccount);

        } else {
            return null;
        }

    }
}