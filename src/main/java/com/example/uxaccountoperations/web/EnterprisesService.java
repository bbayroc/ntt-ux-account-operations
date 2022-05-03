package com.example.uxaccountoperations.web;

import com.example.uxaccountoperations.model.enterprises.EnterpriseResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EnterprisesService {

    @GET("/enterprise/enterprises/{dni}")
    Call<EnterpriseResponse> enterequest(@Path("dni") String dni);
}
