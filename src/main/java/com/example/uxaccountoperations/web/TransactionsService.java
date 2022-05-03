package com.example.uxaccountoperations.web;

import com.example.uxaccountoperations.model.transactions.TransactionRequest;
import com.example.uxaccountoperations.model.transactions.TransactionResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface TransactionsService {

    @GET("/transaction/transactions/{idaccount}")
    Call<List<TransactionResponse>> tranrequest(@Path("idaccount") String idaccount);

    @POST("/transaction/transactions")
    Call<TransactionResponse> trancreate(@Body TransactionRequest transactionRequest);
}
