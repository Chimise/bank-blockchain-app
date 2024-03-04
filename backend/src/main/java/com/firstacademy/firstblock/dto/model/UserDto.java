package com.firstacademy.firstblock.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = { "password", "roles" }, ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private Date dateOfBirth;
    private boolean isAdmin;
    private String presentAddress;
    private String permanentAddress;
    private String city;
    private String postalCode;
    private String country;
    private Date createdAt;
    private Date updatedAt;
    private Collection<RoleDto> roles;

    public String getFullName() {
        return firstName != null ? firstName.concat(" ").concat(lastName) : "";
    }
}
