package com.example.uxdemo.web;

import com.example.uxdemo.model.persons.PersonalResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PersonsService {

    @GET("/personal/personal/{dni}")
    Call<PersonalResponse> persrequest(@Path("dni") String dni);
}
