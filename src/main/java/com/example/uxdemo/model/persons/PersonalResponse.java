package com.example.uxdemo.model.persons;

import lombok.Data;
@Data
public class PersonalResponse {

    private long id;
    private String name;
    private String lastname;
    private String email;
    private String dni;
    private int phone;
    private String clientype;
}