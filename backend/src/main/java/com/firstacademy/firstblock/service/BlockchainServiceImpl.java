package com.firstacademy.firstblock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstacademy.firstblock.dto.model.AccountDto;
import com.firstacademy.firstblock.dto.model.UserDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.hyperledger.fabric.client.Contract;

@Service
public class BlockchainServiceImpl implements BlockchainService {

    @Autowired
    private Contract contract;

    @Override
    public String invokeQuery(String functionName, String... args) throws Exception {
        try {
            byte[] responseBytes = contract.evaluateTransaction(functionName, args);
            return new String(responseBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new Exception("Query failed: " + e.getMessage());
        }
    }

    @Override
    public String invokeTxn(String functionName, String... args) throws Exception {
        try {
            var responseBytes = contract.submitTransaction(functionName, args);
            return new String(responseBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new Exception("Transaction failed: " + e.getMessage());
        }
    }

    @Override
    public String initLedger() throws Exception {
        return invokeTxn("InitLedger");
    }

    @Override
    public AccountDto createAccount(Long userId, String accNo, String accountName, int initialBalance)
            throws Exception {
        String createdAcount = invokeTxn("CreateAccount", String.valueOf(userId), accNo, accountName,
                String.valueOf(initialBalance));
        if (createdAcount != null && createdAcount.length() > 0) {
            return toAccount(createdAcount);
        }
        return null;
    }

    @Override
    public boolean accountExists(String accNo) throws Exception {
        String response = invokeQuery("AccountExists", accNo);
        return Boolean.parseBoolean(response);
    }

    @Override
    public void updateAccount(Long userId, String accNo, String name, String type, String status) throws Exception {
        invokeTxn("UpdateAccount", String.valueOf(userId), accNo, name, type, status);
    }

    @Override
    public String readAccount(String accNo) throws Exception {
        return invokeQuery("ReadAccount", accNo);
    }

    @Override
    public String readTransaction(String transactionId) throws Exception {
        return invokeQuery("ReadTransaction", transactionId);
    }

    @Override
    public String readTransactionHistory(String accNo) throws Exception {
        return invokeQuery("ReadTransactionHistory", accNo);
    }

    @Override
    public String readUserAccounts(Long userId) throws Exception {
        return invokeQuery("ReadUserAccounts", String.valueOf(userId));
    }

    @Override
    public String transferFunds(Long senderUserId, String transactionId, String senderAccNo, String recieverAccNo,
            int amount, String narration, String timestamp) throws Exception {

        return invokeTxn("TransferFunds",
                String.valueOf(senderUserId), transactionId,
                senderAccNo, recieverAccNo, String.valueOf(amount),
                narration, timestamp);
    }

    private AccountDto toAccount(String data) {
        try {
            return new ObjectMapper().readValue(data, AccountDto.class);
        } catch (IOException e) {
            return null;
        }
    }
}
