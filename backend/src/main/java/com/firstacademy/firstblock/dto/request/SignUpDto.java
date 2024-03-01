package com.firstacademy.firstblock.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class SignUpDto {
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Enter a valid email")
    private String email;

    @Size(min = 4, message = "Password should be at least 4 characters")
    private String password;
}
