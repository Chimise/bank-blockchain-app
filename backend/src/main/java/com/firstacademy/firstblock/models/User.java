package com.firstacademy.firstblock.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", )
public class User {
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Id
    private Long Id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private int otp;

    private 

}
