package com.example.uxdemo.business;

import com.example.uxdemo.model.cards.Account;
import com.example.uxdemo.model.cards.DebitcardResponse;
import com.example.uxdemo.model.yanki.YankiResponse;
import com.example.uxdemo.web.YankiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.IOException;


@Service
public class ServiceYankiList {

    @Autowired
    private YankiService yankiService;
    public YankiResponse validator(String identification) throws IOException {

        Call<YankiResponse> call1 = yankiService.yankirequest(identification);

        return call1.execute().body();
    }

    public YankiResponse update(String identification, String idcard) throws IOException {

        Call<YankiResponse> call2 = yankiService.yankiupdate(identification, idcard);

        return call2.execute().body();
    }

}
