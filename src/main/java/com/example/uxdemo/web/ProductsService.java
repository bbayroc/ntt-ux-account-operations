package com.example.uxdemo.web;

import com.example.uxdemo.model.BalanceUpdate;
import com.example.uxdemo.model.products.ProductResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ProductsService {

    @GET("/product/products/idaccounts/{idaccount}")
    Call<ProductResponse> prodrequest(@Path("idaccount") String idaccount);

    @PATCH("/product/products/balance/{idaccount}")
    Call<ProductResponse> produpdate(@Path("idaccount") String idaccount, @Body BalanceUpdate balanceUpdate);

}
