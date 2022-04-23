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

        @PATCH("/product/Products/{idaccount}")
        Call<ProductResponse> produpdate(@Path("idaccount") String idaccount, @Body BalanceUpdate balanceUpdate);


/*

        @POST("/BlackList/{dni}")
        Call<BlackListResponse> blrequest2(@Path("dni") String dni);

        @GET("/BlackList/{dni}")
        Call<BlackListResponse> blrequest(@Path("dni") String dni);
*/
}
