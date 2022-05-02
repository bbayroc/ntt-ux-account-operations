package com.example.uxdemo.util;

import com.example.uxdemo.entity.*;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class ServiceCardList {

    public CardResponse validator(String dni, String idcard, String clienttype) throws IOException {

        RequestService login = RetrofitClienteInstance.getRetrofitPersonal().create(RequestService.class);
        RequestService login2 = RetrofitClienteInstance.getRetrofitEnterprise().create(RequestService.class);
        RequestService login3 = RetrofitClienteInstance.getRetrofitCard().create(RequestService.class);

        Call<PersonalResponse> call = login.persrequest(dni);
        Call<EnterpriseResponse> call2 = login2.enterequest(dni);
        Call<CardResponse> call3 = login3.cardrequest(idcard);

        CardResponse cardResponse = new CardResponse();
        PersonalResponse personalResponse = new PersonalResponse();
        EnterpriseResponse enterpriseResponse = new EnterpriseResponse();

        if (Objects.equals(clienttype, "Personal")) {
            personalResponse = call.execute().body();
        } else if (Objects.equals(clienttype, "Enterprise")) {
            enterpriseResponse = call2.execute().body();
        }

        if (Objects.equals(enterpriseResponse.getDni(), dni) || Objects.equals(personalResponse.getDni(), dni)) {

            cardResponse = call3.execute().body();

        }

        if (Objects.equals(cardResponse.getIdclient(), dni) && Objects.equals(idcard, cardResponse.getIdcard())) {

            return cardResponse;

        } else return null;

    }

    public List<TransactionResponse> getTransaction(String idcard) throws IOException {

        RequestService login4 = RetrofitClienteInstance.getRetrofitTransaction().create(RequestService.class);

        Call<List<TransactionResponse>> call4 = login4.tranrequest(idcard);

        return call4.execute().body();

    }

    public TransactionResponse postTransaction(CardRequest cardRequest, String idcard) throws IOException {

        TransactionRequest transactionRequest = new TransactionRequest();

        transactionRequest.setIdaccount(idcard);
        transactionRequest.setTransactiontype(cardRequest.getTransactiontype());
        transactionRequest.setAmount(cardRequest.getAmount());

        RequestService login5 = RetrofitClienteInstance.getRetrofitTransaction().create(RequestService.class);

        Call<TransactionResponse> call5 = login5.trancreate(transactionRequest);

        return call5.execute().body();

    }

    public CardResponse updateCard(String idcard, BalanceUpdate balanceUpdate) throws IOException {

        RequestService login9 = RetrofitClienteInstance.getRetrofitCard().create(RequestService.class);

        Call<CardResponse> call9 = login9.cardupdate(idcard, balanceUpdate);

        return call9.execute().body();

    }

    public CardResponse cardValidator(String idcard) throws IOException {

        RequestService login7 = RetrofitClienteInstance.getRetrofitCard().create(RequestService.class);

        Call<CardResponse> call7 = login7.cardrequest(idcard);

            return call7.execute().body();

    }

    public DebitcardResponse getdebitcard(String idcard) throws IOException {

        RequestService login10 = RetrofitClienteInstance.getRetrofitCard().create(RequestService.class);

        Call<DebitcardResponse> call10 = login10.debitcardrequest(idcard);

        return call10.execute().body();

    }

    public DebitcardResponse updatedebitcard(String idcard, Account account) throws IOException {

        RequestService login11 = RetrofitClienteInstance.getRetrofitCard().create(RequestService.class);

        Call<DebitcardResponse> call11 = login11.debitcardupdate(idcard, account);

        return call11.execute().body();

    }

}

