package com.example.uxaccountoperations.business;

import com.example.uxaccountoperations.model.cards.CardResponse;
import com.example.uxaccountoperations.model.persons.PersonalResponse;
import com.example.uxaccountoperations.web.CardsService;
import com.example.uxaccountoperations.web.EnterprisesService;
import com.example.uxaccountoperations.web.PersonsService;
import com.example.uxaccountoperations.web.TransactionsService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceCardListTest {

    @InjectMocks
    private ServiceCardList serviceCardList;

    @Mock
    private CardsService cardsService;

    @Mock
    private PersonsService personsService;

    @Mock
    private EnterprisesService enterprisesService;

    @Mock
    private TransactionsService transactionsService;

    @Mock
    private Call call;

    @SneakyThrows
    @Test
    void cardValidatorTest() {


        CardResponse expectedCardResponse = new CardResponse();
        expectedCardResponse.setBalance(200);
        Response<CardResponse> expectedResponse = Response.success(expectedCardResponse);

        when(cardsService.cardrequest(anyString())).thenReturn(call);
        when(call.execute()).thenReturn(expectedResponse);

        CardResponse actualCardResponse = serviceCardList.cardValidator("444");
        assertEquals(actualCardResponse.getBalance(), 200);
    }

    @Test
    void validatorTest() throws IOException {


        PersonalResponse expectedPersonalResponse = new PersonalResponse();
//        expectedCardResponse.setBalance(200);
        Response<PersonalResponse> expectedResponse = Response.success(expectedPersonalResponse);

        when(personsService.persrequest(anyString())).thenReturn(call);
        when(call.execute()).thenReturn(expectedResponse);

        CardResponse actualCardResponse = serviceCardList.validator("111", "444", "Personal");
//        assertEquals(actualCardResponse.getBalance(), 200);
    }
}