package com.example.uxaccountoperations.controller;

import com.example.uxaccountoperations.model.cards.CardResponse;
import com.example.uxaccountoperations.model.products.ProductRequest;
import com.example.uxaccountoperations.model.products.ProductResponse;
import com.example.uxaccountoperations.web.CardsService;
import com.example.uxaccountoperations.web.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Call;

import java.io.IOException;

@RestController
public class ProductController {

    @Autowired
    ProductsService productsService;

    @Autowired
    CardsService cardsservice;

    @PostMapping
    public ProductResponse save(@RequestBody ProductRequest productRequest) throws IOException {

        Call<CardResponse> call1 = cardsservice.cardrequest(productRequest.getIdclient());
        CardResponse cardResponse = call1.execute().body();

        if (cardResponse != null) {

            Call<ProductResponse> call2 = productsService.prodcreate(productRequest);
            ProductResponse productResponse = call2.execute().body();

            return productResponse;
        }
        else return null;
    }

}
