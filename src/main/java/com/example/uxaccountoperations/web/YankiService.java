package com.example.uxaccountoperations.web;

import com.example.uxaccountoperations.model.yanki.YankiRequest;
import com.example.uxaccountoperations.model.yanki.YankiResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface YankiService {

    @GET("/yanki/yanki/identification/{identification}")
    Call<YankiResponse> yankirequest(@Path("identification") String identification);

    @PATCH("/yanki/yanki/{identification}")
    Call<YankiResponse> yankiupdate(@Path("identification") String identification, @Body YankiRequest yankirequest );

}
