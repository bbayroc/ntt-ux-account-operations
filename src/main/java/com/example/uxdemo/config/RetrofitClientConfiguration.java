package com.example.uxdemo.config;

import com.example.uxdemo.web.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retrofit.CircuitBreakerCallAdapter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.uxdemo.util.Constants.*;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

@Configuration
public class RetrofitClientConfiguration {

    @Autowired
    private CircuitBreaker circuitBreakerRest;

    private final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(BODY);
    private final OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    private final Gson gson = new GsonBuilder().setLenient().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Bean
    ProductsService productsService() {
        return new Retrofit.Builder()
                .baseUrl(PRODUCT_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CircuitBreakerCallAdapter.of(circuitBreakerRest))
                .build()
                .create(ProductsService.class);
    }

    @Bean
    TransactionsService transactionsService() {
        return new Retrofit.Builder()
                .baseUrl(TRANSACTION_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CircuitBreakerCallAdapter.of(circuitBreakerRest))
                .build()
                .create(TransactionsService.class);
    }

    @Bean
    PersonsService personsService() {
        return new Retrofit.Builder()
                .baseUrl(PERSONAL_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CircuitBreakerCallAdapter.of(circuitBreakerRest, r -> r.code() < 500))
                .build()
                .create(PersonsService.class);
    }

    @Bean
    EnterprisesService enterprisesService() {
        return new Retrofit.Builder()
                .baseUrl(ENTERPRISE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CircuitBreakerCallAdapter.of(circuitBreakerRest))
                .build()
                .create(EnterprisesService.class);
    }

    @Bean
    CardsService service() {
        return new Retrofit.Builder()
                .baseUrl(CARD_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CircuitBreakerCallAdapter.of(circuitBreakerRest))
                .build()
                .create(CardsService.class);
    }
}