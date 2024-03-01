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
@Table(name = "users", indexes = { @Index(name = "idx_user_email", columnList = "email", unique = true),
        @Index(name = "idx_user_username", columnList = "username", unique = true) })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "user_id")
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String presentAddress;

    private String permanentAddress;

    private String city;

    private String country;

    private Date dateOfBirth;

    private String password;

    private String postalCode;

    private String otp;

    private Date otpExpirationDate;

    private boolean isSuspended = false;

    private Date lastLoggedIn;

    private boolean isActive = true;

    private String phoneNumber;

    private Date createdAt;

    private Date updatedAt;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)

    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Collection<Role> roles;
}
