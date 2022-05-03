package com.example.uxaccountoperations.web;

import com.example.uxaccountoperations.model.yanki.YankiRequest;
import com.example.uxaccountoperations.model.yanki.YankiResponse;
import com.example.uxaccountoperations.model.yanki.YankiTransaction;
import retrofit2.Call;
import retrofit2.http.*;

public interface YankiService {

    @POST("/yanki/yanki/transaction/{cellphone}")
    Call<YankiTransaction> yankitransaction(@Path("cellphone") String cellphone, @Body YankiTransaction yankiTransaction);

    @GET("/yanki/yanki/identification/{identification}")
    Call<YankiResponse> yankirequest(@Path("identification") String identification);

    @GET("/yanki/yanki/{cellphone}")
    Call<YankiResponse> yankirequest2(@Path("cellphone") String identification);

    @PATCH("/yanki/yanki/{identification}")
    Call<YankiResponse> yankiupdate(@Path("identification") String identification, @Body YankiRequest yankirequest );

}
