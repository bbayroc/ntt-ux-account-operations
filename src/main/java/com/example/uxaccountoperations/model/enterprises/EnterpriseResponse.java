package com.example.uxaccountoperations.model.enterprises;

import lombok.Data;

@Data
public class EnterpriseResponse {

    private long id;
    private String name;
    private String lastname;
    private String email;
    private String dni;
    private int phone;
    private String clienttype;
    private String signer;
}
