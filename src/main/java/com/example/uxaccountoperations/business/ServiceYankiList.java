package com.example.uxaccountoperations.business;

import com.example.uxaccountoperations.model.yanki.YankiRequest;
import com.example.uxaccountoperations.model.yanki.YankiResponse;
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

    public YankiResponse update(String identification, YankiRequest yankirequest) throws IOException {

        Call<YankiResponse> call2 = yankiService.yankiupdate(identification, yankirequest);

        return call2.execute().body();
    }

}
