package com.example.uxdemo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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