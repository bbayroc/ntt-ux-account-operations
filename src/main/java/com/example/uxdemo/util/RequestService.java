package com.example.uxdemo.util;

import com.example.uxdemo.entity.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface RequestService {

        @GET("/Products/Idaccounts/{idaccount}")
        Call<ProductResponse> prodrequest(@Path("idaccount") String idaccount);

        @GET("/Personal/{dni}")
        Call<PersonalResponse> persrequest(@Path("dni") String dni);

        @GET("/Enterprises/{dni}")
        Call<EnterpriseResponse> enterequest(@Path("dni") String dni);

        @GET("/Transactions/{idaccount}")
        Call<List<TransactionResponse>> tranrequest(@Path("idaccount") String idaccount);

        @POST("/Transactions")
        Call<TransactionResponse> trancreate(@Body TransactionRequest transactionRequest);

        @PATCH("/Products/{idaccount}")
        Call<ProductResponse> produpdate(@Path("idaccount") String idaccount, @Body BalanceUpdate balanceUpdate);


/*

        @POST("/BlackList/{dni}")
        Call<BlackListResponse> blrequest2(@Path("dni") String dni);

        @GET("/BlackList/{dni}")
        Call<BlackListResponse> blrequest(@Path("dni") String dni);
*/
}
