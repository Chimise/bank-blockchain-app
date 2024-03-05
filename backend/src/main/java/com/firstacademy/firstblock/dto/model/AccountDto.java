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

    private int userId;
    private String accNo;
    private String accountName;
    private int initialBalance;
}
