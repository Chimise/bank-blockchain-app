package com.firstacademy.firstblock.dto.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class TransactionDto {
    private String type;
    private String id;
    private String docType;
    private String from;
    private long amount;
    private String createdAt;
    private String description;
    private String to;
    private String currency;
    private String mode;
}
