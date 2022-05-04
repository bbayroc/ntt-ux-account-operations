package com.example.uxaccountoperations.business;

import com.example.uxaccountoperations.infraestructure.events.BalanceUpdate;
import com.example.uxaccountoperations.infraestructure.events.TransactionEvent;
import com.example.uxaccountoperations.infraestructure.producer.TransactionEventProducer;
import com.example.uxaccountoperations.model.products.ProductRequest;
import com.example.uxaccountoperations.model.products.ProductResponse;
import com.example.uxaccountoperations.model.transactions.TransactionRequest;
import com.example.uxaccountoperations.model.transactions.TransactionResponse;
import com.example.uxaccountoperations.web.ProductsService;
import com.example.uxaccountoperations.web.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ServiceKafka {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private TransactionsService transactionsService;

    @Autowired
    private TransactionEventProducer transactionEventProducer;

    public TransactionResponse transactionValidator(ProductRequest productRequest, ProductResponse productResponse) throws IOException {

        BalanceUpdate balanceUpdate = new BalanceUpdate();

        //Valida la cuenta y que los retiros sean numeros negativos
        if (productResponse == null || (Objects.equals(productRequest.getTransactiontype(), "Retiro") && productRequest.getAmount() >= 0) || (Objects.equals(productRequest.getTransactiontype(), "Deposito") && productRequest.getAmount() <= 0)) {
            return null;
        }
        else
            //Valida que el retiro no sea mayor al balance
            if ((productResponse.getBalance() + productRequest.getAmount()) < 0 || (limitValidator(productResponse.getIdaccount()) && !Set.of("PYME", "VIP").contains(productResponse.getAccounttype()))) {
                return null;
            }
            else {
                balanceUpdate.setBalance(productRequest.getAmount());
            }

        if (!Objects.equals(productResponse.getAccounttype(), "Cuenta Corriente") && limitValidator(productResponse.getIdaccount())) {
            balanceUpdate.setBalance(productRequest.getAmount() - productResponse.getComission());
            productRequest.setAppliedcomision(-productResponse.getComission());
        }

        TransactionRequest transactionRequest = new TransactionRequest();
        TransactionEvent transactionEvent = new TransactionEvent();

        transactionEvent.setBalance(balanceUpdate);
        transactionEvent.setIdaccount(productResponse.getIdaccount());
        transactionEvent.setTransactiontype(productRequest.getTransactiontype());
        transactionEvent.setAmount(productRequest.getAmount());
        transactionEvent.setAppliedcomission(productRequest.getAppliedcomision());
/*
        transactionRequest.setIdaccount(productResponse.getIdaccount());
        transactionRequest.setTransactiontype(productRequest.getTransactiontype());
        transactionRequest.setAmount(productRequest.getAmount());
        transactionRequest.setAppliedcomission(productRequest.getAppliedcomision());

        Call<ProductResponse> call6 = productsService.produpdate(transactionRequest.getIdaccount(), balanceUpdate);
        call6.execute().body();
        Call<TransactionResponse> call5 = transactionsService.trancreate(transactionRequest);
        call5.execute().body();
 */
        transactionEventProducer.produce("TransferEvent", transactionEvent);

        return null;
    }


    public boolean limitValidator(String idaccount) throws IOException {

        Call<ProductResponse> call7 = productsService.prodrequest(idaccount);

        ProductResponse productResponse = call7.execute().body();

        if (productResponse.getMovementlimit() > 0) {

            List<TransactionResponse> transactions = getTransaction(idaccount);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            long count = transactions.stream()
                    .map(c -> LocalDateTime.parse(c.getCreated(), formatter).toLocalDate().getMonth())
                    .filter(c -> c.equals(LocalDateTime.now().getMonth()))
                    .count();

            return (count >= productResponse.getMovementlimit() || (Objects.equals(productResponse.getAccounttype(), "Plazo Fijo")) && productResponse.getUniquedayofmovement() != LocalDateTime.now().getDayOfMonth());

        }
        else {
            return false;
        }
    }

    public List<TransactionResponse> getTransaction(String idaccount) throws IOException {

        Call<List<TransactionResponse>> call4 = transactionsService.tranrequest(idaccount);

        return call4.execute().body();

    }

}
