package com.example.uxaccountoperations.business;

import com.example.uxaccountoperations.model.BalanceUpdate;
import com.example.uxaccountoperations.model.enterprises.EnterpriseResponse;
import com.example.uxaccountoperations.model.persons.PersonalResponse;
import com.example.uxaccountoperations.model.products.ProductRequest;
import com.example.uxaccountoperations.model.products.ProductResponse;
import com.example.uxaccountoperations.model.transactions.TransactionRequest;
import com.example.uxaccountoperations.model.transactions.TransactionResponse;
import com.example.uxaccountoperations.web.CardsService;
import com.example.uxaccountoperations.web.EnterprisesService;
import com.example.uxaccountoperations.web.PersonsService;
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
public class ServiceList {

    @Autowired
    private PersonsService personsService;

    @Autowired
    private EnterprisesService enterprisesService;

    @Autowired
    private CardsService cardsService;

    @Autowired
    private TransactionsService transactionsService;

    @Autowired
    private ProductsService productsService;

    public ProductResponse validator(String dni, String idaccount, String clienttype) throws IOException {

        Call<PersonalResponse> call = personsService.persrequest(dni);
        Call<EnterpriseResponse> call2 = enterprisesService.enterequest(dni);
        Call<ProductResponse> call3 = productsService.prodrequest(idaccount);

        ProductResponse productResponse = new ProductResponse();
        PersonalResponse personalResponse = new PersonalResponse();
        EnterpriseResponse enterpriseResponse = new EnterpriseResponse();

        if (Objects.equals(clienttype, "Personal")) {
            personalResponse = call.execute().body();
        }
        else if (Objects.equals(clienttype, "Enterprise")) {
            enterpriseResponse = call2.execute().body();
        }

        if (Objects.equals(enterpriseResponse.getDni(), dni) || Objects.equals(personalResponse.getDni(), dni)) {

            productResponse = call3.execute().body();

        }

        if (Objects.equals(productResponse.getIdclient(), dni) && Objects.equals(idaccount, productResponse.getIdaccount())) {
            return productResponse;
        }
        else {
            return null;
        }
    }

    public List<TransactionResponse> getTransaction(String idaccount) throws IOException {

        Call<List<TransactionResponse>> call4 = transactionsService.tranrequest(idaccount);

        return call4.execute().body();

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

    public ProductResponse updateProduct(String idaccount, BalanceUpdate balanceUpdate) throws IOException {

        Call<ProductResponse> call6 = productsService.produpdate(idaccount, balanceUpdate);
        return call6.execute().body();
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

            return (count >= productResponse.getMovementlimit() || (Objects.equals(productResponse.getAccounttype(), "Plazo Fijo")) &&
                    productResponse.getUniquedayofmovement() != LocalDateTime.now().getDayOfMonth());

        }
        else {
            return false;
        }
    }

    public ProductResponse accountValidator(String idaccount) throws IOException {

        Call<ProductResponse> call8 = productsService.prodrequest(idaccount);
        return call8.execute().body();
    }

    public TransactionResponse transactionValidator(ProductRequest productRequest, ProductResponse productResponse) throws IOException {

        BalanceUpdate balanceUpdate = new BalanceUpdate();

        //Valida la cuenta y que los retiros sean numeros negativos
        if (productResponse == null || (Objects.equals(productRequest.getTransactiontype(), "Retiro") && productRequest.getAmount() >= 0) ||
                (Objects.equals(productRequest.getTransactiontype(), "Deposito") && productRequest.getAmount() <= 0)) {
            return null;
        }
        else
            //Valida que el retiro no sea mayor al balance
            if ((productResponse.getBalance() + productRequest.getAmount()) < 0 ||
                    (limitValidator(productResponse.getIdaccount()) && !Set.of("PYME", "VIP").contains(productResponse.getAccounttype()))) {
                return null;
            }
            else {
                balanceUpdate.setBalance(productRequest.getAmount());
            }

        if (!Objects.equals(productResponse.getAccounttype(), "Cuenta Corriente") && limitValidator(productResponse.getIdaccount())) {
            balanceUpdate.setBalance(productRequest.getAmount() - productResponse.getComission());
            productRequest.setAppliedcomision(-productResponse.getComission());
        }
        updateProduct(productResponse.getIdaccount(), balanceUpdate);

        return postTransaction(productRequest, productResponse.getIdaccount());
    }


}