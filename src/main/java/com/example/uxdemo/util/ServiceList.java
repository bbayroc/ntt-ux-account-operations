package com.example.uxdemo.util;

import com.example.uxdemo.entity.*;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class ServiceList {

    public ProductResponse Validator(String dni, String idaccount, String idclient) throws IOException {

        RequestService login = RetrofitClienteInstance.getRetrofitPersonal().create(RequestService.class);
        RequestService login2 = RetrofitClienteInstance.getRetrofitEnterprise().create(RequestService.class);
        RequestService login3 = RetrofitClienteInstance.getRetrofitProduct().create(RequestService.class);

        Call<PersonalResponse> call = login.persrequest(dni);
        Call<EnterpriseResponse> call2 = login2.enterequest(dni);
        Call<ProductResponse> call3 = login3.prodrequest(idaccount);

        ProductResponse productResponse = new ProductResponse();
        PersonalResponse personalResponse = new PersonalResponse();
        EnterpriseResponse enterpriseResponse = new EnterpriseResponse();

        if (Objects.equals(idclient, "Personal")) {
            personalResponse = call.execute().body();
        } else if (Objects.equals(idclient, "Enterprise")) {
            enterpriseResponse = call2.execute().body();
        }

        if (Objects.equals(enterpriseResponse.getDni(), dni) || Objects.equals(personalResponse.getDni(), dni)) {

            productResponse = call3.execute().body();

        }

        if (Objects.equals(productResponse.getIdclient(), dni) && Objects.equals(idaccount, productResponse.getIdaccount())) {

            return productResponse;

        } else return null;

    }

    public List<TransactionResponse> getTransaction(String idaccount) throws IOException {

        RequestService login4 = RetrofitClienteInstance.getRetrofitTransaction().create(RequestService.class);

        Call<List<TransactionResponse>> call4 = login4.tranrequest(idaccount);

        return call4.execute().body();

    }

    public TransactionResponse postTransaction(ProductRequest productRequest, String idaccount) throws IOException {

        TransactionRequest transactionRequest = new TransactionRequest();

        transactionRequest.setIdaccount(idaccount);
        transactionRequest.setTransactiontype(productRequest.getTransactiontype());
        transactionRequest.setAmount(productRequest.getAmount());
        transactionRequest.setAppliedcomission(productRequest.getAppliedcomision());

        RequestService login5 = RetrofitClienteInstance.getRetrofitTransaction().create(RequestService.class);

        Call<TransactionResponse> call5 = login5.trancreate(transactionRequest);

        return call5.execute().body();

    }

    public ProductResponse updateProduct(String idaccount, BalanceUpdate balanceUpdate) throws IOException {

        RequestService login6 = RetrofitClienteInstance.getRetrofitProduct().create(RequestService.class);

        Call<ProductResponse> call6 = login6.produpdate(idaccount, balanceUpdate);

        return call6.execute().body();

    }


    public boolean limitValidator(String idaccount) throws IOException {

        RequestService login7 = RetrofitClienteInstance.getRetrofitProduct().create(RequestService.class);

        Call<ProductResponse> call7 = login7.prodrequest(idaccount);

        ProductResponse productResponse = call7.execute().body();

        if (productResponse.getMovementlimit() > 0) {

            List<TransactionResponse> transactions = getTransaction(idaccount);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            long count = transactions.stream()
                    .map(c -> LocalDateTime.parse(c.getCreated(), formatter).toLocalDate().getMonth())
                    .filter((c) -> c.equals(LocalDateTime.now().getMonth()))
                    .count();

            return (count >= productResponse.getMovementlimit() || (Objects.equals(productResponse.getAccounttype(), "Plazo Fijo")) && productResponse.getUniquedayofmovement() != LocalDateTime.now().getDayOfMonth());

        }

        else return false;
    }

    public ProductResponse accountValidator(String idaccount) throws IOException {

        RequestService login8 = RetrofitClienteInstance.getRetrofitProduct().create(RequestService.class);

        Call<ProductResponse> call8 = login8.prodrequest(idaccount);

            return call8.execute().body();

    }

}

