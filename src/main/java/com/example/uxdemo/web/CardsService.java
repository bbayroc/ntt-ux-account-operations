package com.example.uxdemo.web;

import com.example.uxdemo.model.cards.Account;
import com.example.uxdemo.model.BalanceUpdate;
import com.example.uxdemo.model.cards.CardResponse;
import com.example.uxdemo.model.cards.DebitcardResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface CardsService {

    @PATCH("/card/cards/{idaccount}")
    Call<CardResponse> cardupdate(@Path("idaccount") String idaccount, @Body BalanceUpdate balanceUpdate);

    @GET("/card/cards/idcards/{idcard}")
    Call<CardResponse> cardrequest(@Path("idcard") String idcard);

    @GET("/card/debit/idcards/{idcard}")
    Call<DebitcardResponse> debitcardrequest(@Path("idcard") String idcard);

    @PATCH("/card/debit/{idcard}")
    Call<DebitcardResponse> debitcardupdate(@Path("idcard") String idcard, @Body Account account);
}
