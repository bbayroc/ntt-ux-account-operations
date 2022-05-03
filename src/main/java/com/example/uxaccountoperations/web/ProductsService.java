package com.example.uxaccountoperations.web;

import com.example.uxaccountoperations.model.BalanceUpdate;
import com.example.uxaccountoperations.model.products.ProductRequest;
import com.example.uxaccountoperations.model.products.ProductResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface ProductsService {

    @GET("/product/products/idaccounts/{idaccount}")
    Call<ProductResponse> prodrequest(@Path("idaccount") String idaccount);

    @POST("/product/products}")
    Call<ProductResponse> prodcreate(@Body ProductRequest productRequest);

    @PATCH("/product/products/balance/{idaccount}")
    Call<ProductResponse> produpdate(@Path("idaccount") String idaccount, @Body BalanceUpdate balanceUpdate);

}
