package com.firstacademy.firstblock.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firstacademy.firstblock.dto.model.TransferRequest;
import com.firstacademy.firstblock.service.BlockchainService;

@RestController
@RequestMapping("/api/transfers")
public class TransactionController {

    @Autowired
    private BlockchainService blockchainService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(
            @RequestBody @Valid TransferRequest request,
            BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessage(bindingResult));
        }

        String response = blockchainService.transferFunds(
                request.getSenderUserId(),
                request.getTransactionId(),
                request.getSenderAccountNumber(),
                request.getReceiverAccountNumber(),
                request.getAmount(),
                request.getNarration(),
                request.getTimestamp());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<String> getTransactionHistory(@PathVariable String accountNumber) throws Exception {
        String transactionHistory = blockchainService.readTransactionHistory(accountNumber);
        return ResponseEntity.ok(transactionHistory);
    }

    private String getErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n");
        }
        return errorMessage.toString();
    }
}

