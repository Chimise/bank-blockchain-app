package com.firstacademy.firstblock.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class TransferRequest {

    private Long userId;

    private String transactionDate;

    private String transactionId;

    @NotBlank(message = "Sender account number is required")
    private String senderAccountNumber;

    @NotBlank(message = "Receiver account number is required")
    private String receiverAccountNumber;

    @NotNull(message = "Transfer amount is required")
    @Min(value = 50, message = "Minimum Transfer allowed is 50 Naira")
    private Integer amount;

    @NotBlank(message = "Narration for the transfer is required")
    private String narration;
}
