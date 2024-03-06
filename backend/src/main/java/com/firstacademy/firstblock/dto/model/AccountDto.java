package com.firstacademy.firstblock.dto.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AccountDto {
    private String accNo;
    private int bal;
    private String createdAt;
    private String currency;
    private long dailyLimit;
    private String docType;
    private String name;
    private String status;
    private long transactionLimit;
    private String type;
    private String updatedAt;
    private long userId;
}
