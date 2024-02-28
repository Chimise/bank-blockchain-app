package com.firstacademy.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.logging.log4j.util.Supplier;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    
    // Constructors

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User orElseThrow(Supplier<? extends RuntimeException> exceptionSupplier) {
    // Assuming this is a placeholder method within a class that mimics Optional behavior,
    // you should implement the logic to either return the User or throw the exception provided by the supplier.
    throw exceptionSupplier.get();
}

}
