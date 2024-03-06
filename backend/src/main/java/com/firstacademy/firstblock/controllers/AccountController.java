package com.firstacademy.firstblock.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firstacademy.firstblock.dto.model.AccountDto;
import com.firstacademy.firstblock.dto.request.CreateAccountRequest;
import com.firstacademy.firstblock.dto.response.Response;
import com.firstacademy.firstblock.service.BlockchainService;
import com.firstacademy.firstblock.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlockchainService blockchainService;

    @GetMapping
    public Response<?> getUserAccounts() throws Exception {
        var user = userService.getCurrentUser();
        AccountDto[] response = blockchainService.readUserAccounts(user.getId());
        return Response.ok().setPayload(response);
    }

    @GetMapping("/{accountNo}")
    public Response<?> getAccount(@PathVariable("accountNo") String accountNo) throws Exception {
        AccountDto response = blockchainService.readAccount(accountNo);
        return Response.ok().setPayload(response);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Response<?> createAccount(@Valid @RequestBody CreateAccountRequest request,
            BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return Response.badRequest().setErrors(bindingResult.getFieldErrors());
        }

        request.setInitialBalance(request.getInitialBalance() * 100);

        AccountDto response = blockchainService.createAccount(request.getUserId(), request.getAccountNo(),
                request.getAccountName(), request.getInitialBalance());

        return Response.ok().setPayload(response);
    }
}
