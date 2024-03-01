package com.firstacademy.firstblock.dto.request;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.firstacademy.firstblock.dto.model.UserDto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateProfileRequest {
    @Size(min = 5, message = "Name should be at least three characters")
    private String name;

    @NotBlank
    @Size(min = 3, message = "Username should be at least three characters")
    private String userName;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Size(min = 4, message = "Present address should have a minimum of four characters")
    private String presentAddress;

    @Size(min = 4, message = "Permanent address should have a minimum of four characters")
    private String permanentAddress;

    @Size(min = 2, message = "Enter a valid city")
    private String city;

    @Size(min = 4, message = "Enter a valid postal code")
    private String postalCode;

    @Size(min = 2, message = "Enter a valid country")
    private String country;

    public String getFirstName() {
        String[] names = name.split(" ");
        return names.length >= 1 ? names[0] : null;
    }

    public String getLastName() {
        String[] names = name.split(" ");
        return names.length >= 2 ? names[1] : null;
    }

    public UserDto build(UserDto userDto) {
        if (hasValue(userName)) {
            userDto.setUsername(this.getUserName());
        }

        if (hasValue(dateOfBirth)) {
            userDto.setDateOfBirth(this.getDateOfBirth());
        }

        if (hasValue(presentAddress)) {
            userDto.setPresentAddress(presentAddress);
        }

        if (hasValue(permanentAddress)) {
            userDto.setPermanentAddress(permanentAddress);
        }

        if (hasValue(city)) {
            userDto.setCity(city);
        }

        if (hasValue(postalCode)) {
            userDto.setPostalCode(postalCode);
        }

        if (hasValue(country)) {
            userDto.setCountry(country);
        }

        if (hasValue(getFirstName())) {
            userDto.setFirstName(getFirstName());
        }

        if (hasValue(getLastName())) {
            userDto.setLastName(getLastName());
        }

        return userDto;
    }

    private boolean hasValue(Object data) {
        return data != null;
    }
}
