package com.example.uxaccountoperations.business;

import com.example.uxaccountoperations.model.yanki.YankiRequest;
import com.example.uxaccountoperations.model.yanki.YankiResponse;
import com.example.uxaccountoperations.model.yanki.YankiTransaction;
import com.example.uxaccountoperations.web.YankiService;
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

    public YankiResponse validatorcellphone(String cellphone) throws IOException {

        Call<YankiResponse> call4 = yankiService.yankirequest2(cellphone);

        return call4.execute().body();
    }

    public YankiResponse update(String identification, YankiRequest yankirequest) throws IOException {

        Call<YankiResponse> call2 = yankiService.yankiupdate(identification, yankirequest);

        return call2.execute().body();
    }

    public YankiTransaction transaction(String cellphone, YankiTransaction yankiTransaction) throws IOException {

        Call<YankiTransaction> call3 = yankiService.yankitransaction(cellphone, yankiTransaction);

        return call3.execute().body();
    }

}
