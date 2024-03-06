package com.firstacademy.firstblock.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CreateAccountRequest {
    @Min(value = 1, message = "Number must be positive")
    private long userId;

    @NotBlank(message = "Account name must be provided")
    private String accountName;

    @NotBlank(message = "Account No is required")
    private String accountNo;

    @Min(value = 50, message = "Should have a minimum value of 1")
    private int initialBalance;
}
