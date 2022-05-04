package com.example.uxaccountoperations.business;

import com.example.uxaccountoperations.model.BalanceUpdate;
import com.example.uxaccountoperations.model.TransactionEvent;
import com.example.uxaccountoperations.model.products.ProductRequest;
import com.example.uxaccountoperations.model.products.ProductResponse;
import com.example.uxaccountoperations.model.transactions.TransactionRequest;
import com.example.uxaccountoperations.model.transactions.TransactionResponse;
import com.example.uxaccountoperations.web.ProductsService;
import com.example.uxaccountoperations.web.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Call;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ServiceKafka {


    @Autowired
    private ProductsService productsService;

    @Autowired
    private TransactionsService transactionsService;

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

        transactionRequest.setIdaccount(productResponse.getIdaccount());
        transactionRequest.setTransactiontype(productRequest.getTransactiontype());
        transactionRequest.setAmount(productRequest.getAmount());
        transactionRequest.setAppliedcomission(productRequest.getAppliedcomision());

        Call<ProductResponse> call6 = productsService.produpdate(transactionRequest.getIdaccount(), balanceUpdate);

        call6.execute().body();



        Call<TransactionResponse> call5 = transactionsService.trancreate(transactionRequest);

        call5.execute().body();

        return null;
    }


    public boolean limitValidator(String idaccount) throws IOException {

        Call<ProductResponse> call7 = productsService.prodrequest(idaccount);

        ProductResponse productResponse = call7.execute().body();

        if (productResponse.getMovementlimit() > 0) {

            Call<List<TransactionResponse>> call4 = transactionsService.tranrequest(idaccount);
            List<TransactionResponse> transactions = call4.execute().body();
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

    public TransactionResponse postTransaction(ProductRequest productRequest, String idaccount) throws IOException {

        TransactionRequest transactionRequest = new TransactionRequest();

        transactionRequest.setIdaccount(idaccount);
        transactionRequest.setTransactiontype(productRequest.getTransactiontype());
        transactionRequest.setAmount(productRequest.getAmount());
        transactionRequest.setAppliedcomission(productRequest.getAppliedcomision());

        Call<TransactionResponse> call5 = transactionsService.trancreate(transactionRequest);

        return call5.execute().body();
    }

}
