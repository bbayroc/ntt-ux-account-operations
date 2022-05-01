package com.example.uxdemo.util;

import com.example.uxdemo.entity.*;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface RequestService {

        @GET("/product/Products/Idaccounts/{idaccount}")
        Call<ProductResponse> prodrequest(@Path("idaccount") String idaccount);

        @GET("/personal/Personal/{dni}")
        Call<PersonalResponse> persrequest(@Path("dni") String dni);

        @GET("/enterprise/Enterprises/{dni}")
        Call<EnterpriseResponse> enterequest(@Path("dni") String dni);

        @GET("/transaction/Transactions/{idaccount}")
        Call<List<TransactionResponse>> tranrequest(@Path("idaccount") String idaccount);

        @POST("/transaction/Transactions")
        Call<TransactionResponse> trancreate(@Body TransactionRequest transactionRequest);

        @PATCH("/product/Products/Balance/{idaccount}")
        Call<ProductResponse> produpdate(@Path("idaccount") String idaccount, @Body BalanceUpdate balanceUpdate);

        @PATCH("/card/Cards/{idaccount}")
        Call<CardResponse> cardupdate(@Path("idaccount") String idaccount, @Body BalanceUpdate balanceUpdate);

        @GET("/card/Cards/Idcards/{idcard}")
        Call<CardResponse> cardrequest(@Path("idcard") String idcard);

        @GET("/card/Debit/Idcards/{idcard}")
        Call<DebitcardResponse> debitcardrequest(@Path("idcard") String idcard);

        @PATCH("/card/Debit/{idcard}")
        Call<DebitcardResponse> debitcardupdate(@Path("idcard") String idcard, @Body Account account);

        @GET("/Yanki/{cellphone}")
        Call<YankiResponse> yankirequest(@Path("cellphone") String cellphone);

}
