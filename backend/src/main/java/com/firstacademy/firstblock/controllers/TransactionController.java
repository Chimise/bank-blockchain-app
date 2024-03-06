package com.firstacademy.firstblock.controllers;

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

import jakarta.validation.Valid;
import java.util.UUID;

import com.firstacademy.firstblock.dto.model.TransactionDto;
import com.firstacademy.firstblock.dto.model.UserDto;
import com.firstacademy.firstblock.dto.request.TransferRequest;
import com.firstacademy.firstblock.dto.response.Response;
import com.firstacademy.firstblock.service.BlockchainService;
import com.firstacademy.firstblock.service.UserService;
import static com.firstacademy.firstblock.util.DateUtils.currentDateIso;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private UserService userService;

    @PostMapping("/transfer")
    public Response<?> transferFunds(
            @RequestBody @Valid TransferRequest request,
            BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return Response.badRequest().setErrors(getErrorMessage(bindingResult));
        }

        UserDto user = userService.getCurrentUser();

        request.setAmount(request.getAmount() * 100).setUserId(user.getId()).setTransactionDate(currentDateIso())
                .setTransactionId(UUID.randomUUID().toString());

        TransactionDto response = blockchainService.transferFunds(
                request.getUserId(),
                request.getTransactionId(),
                request.getSenderAccountNumber(),
                request.getReceiverAccountNumber(),
                request.getAmount(),
                request.getNarration(),
                request.getTransactionDate());

        return Response.ok().setPayload(response);
    }

    @GetMapping("/account/{accountNo}")
    public Response<?> getTransactionHistory(@PathVariable String accountNo) throws Exception {
        TransactionDto[] transactionHistory = blockchainService.readTransactionHistory(accountNo);
        return Response.ok().setPayload(transactionHistory);
    }

    @GetMapping("/{transactionId}")
    public Response<?> getTransactionDetails(@PathVariable("transactionId") String transactionId) throws Exception {
        TransactionDto transaction = blockchainService.readTransaction(transactionId);
        return Response.ok().setPayload(transaction);
    }

    private String getErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n");
        }
        return errorMessage.toString();
    }
}
