package com.firstacademy.firstblock.model;

import jakarta.persistence.*;
import java.util.Date;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.ToString;

import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "users", indexes = @Index(name = "idx_user_email", columnList = "email", unique = true))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String address;

    private Date dateOfBirth;

    private String password;

    private String otp;

    private Date otpExpirationDate;

    private boolean isSuspended = false;

    private Date lastLoggedIn;

    private boolean isActive = true;

    private String phoneNumber;

    private Date createdAt;

    private Date updatedAt;

    @ManyToMany()
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Collection<Role> roles;
}
