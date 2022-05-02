package com.example.uxdemo.business;

import com.example.uxdemo.model.BalanceUpdate;
import com.example.uxdemo.model.cards.Account;
import com.example.uxdemo.model.cards.CardRequest;
import com.example.uxdemo.model.cards.CardResponse;
import com.example.uxdemo.model.cards.DebitcardResponse;
import com.example.uxdemo.model.enterprises.EnterpriseResponse;
import com.example.uxdemo.model.persons.PersonalResponse;
import com.example.uxdemo.model.transactions.TransactionRequest;
import com.example.uxdemo.model.transactions.TransactionResponse;
import com.example.uxdemo.web.CardsService;
import com.example.uxdemo.web.EnterprisesService;
import com.example.uxdemo.web.PersonsService;
import com.example.uxdemo.web.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class ServiceCardList {

    @Autowired
    private PersonsService personsService;

    @Autowired
    private EnterprisesService enterprisesService;

    @Autowired
    private CardsService cardsService;

    @Autowired
    private TransactionsService transactionsService;

    public CardResponse validator(String dni, String idcard, String clienttype) throws IOException {

        Call<PersonalResponse> call = personsService.persrequest(dni);
        Call<EnterpriseResponse> call2 = enterprisesService.enterequest(dni);
        Call<CardResponse> call3 = cardsService.cardrequest(idcard);

        CardResponse cardResponse = new CardResponse();
        PersonalResponse personalResponse = new PersonalResponse();
        EnterpriseResponse enterpriseResponse = new EnterpriseResponse();

        if (Objects.equals(clienttype, "Personal")) {
            personalResponse = call.execute().body();
        }
        else if (Objects.equals(clienttype, "Enterprise")) {
            enterpriseResponse = call2.execute().body();
        }

        if (Objects.equals(enterpriseResponse.getDni(), dni) || Objects.equals(personalResponse.getDni(), dni)) {
            cardResponse = call3.execute().body();
        }

        if (Objects.equals(cardResponse.getIdclient(), dni) && Objects.equals(idcard, cardResponse.getIdcard())) {
            return cardResponse;
        }
        else {
            return null;
        }
    }

    public List<TransactionResponse> getTransaction(String idcard) throws IOException {

        Call<List<TransactionResponse>> call4 = transactionsService.tranrequest(idcard);
        return call4.execute().body();
    }

    public TransactionResponse postTransaction(CardRequest cardRequest, String idcard) throws IOException {

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setIdaccount(idcard);
        transactionRequest.setTransactiontype(cardRequest.getTransactiontype());
        transactionRequest.setAmount(cardRequest.getAmount());

        Call<TransactionResponse> call5 = transactionsService.trancreate(transactionRequest);
        return call5.execute().body();
    }

    public CardResponse updateCard(String idcard, BalanceUpdate balanceUpdate) throws IOException {

        Call<CardResponse> call9 = cardsService.cardupdate(idcard, balanceUpdate);
        return call9.execute().body();
    }

    public CardResponse cardValidator(String idcard) throws IOException {

        Call<CardResponse> call7 = cardsService.cardrequest(idcard);
        return call7.execute().body();
    }

    public DebitcardResponse getdebitcard(String idcard) throws IOException {

        Call<DebitcardResponse> call10 = cardsService.debitcardrequest(idcard);
        return call10.execute().body();
    }

    public DebitcardResponse updatedebitcard(String idcard, Account account) throws IOException {

        Call<DebitcardResponse> call11 = cardsService.debitcardupdate(idcard, account);
        return call11.execute().body();
    }
}