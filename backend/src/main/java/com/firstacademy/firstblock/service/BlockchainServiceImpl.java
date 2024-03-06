package com.firstacademy.firstblock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstacademy.firstblock.dto.model.AccountDto;
import com.firstacademy.firstblock.dto.model.TransactionDto;
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
        return parseTAccountDto(createdAcount);
    }

    @Override
    public boolean accountExists(String accNo) throws Exception {
        String response = invokeQuery("AccountExists", accNo);
        return Boolean.parseBoolean(response);
    }

    @Override
    public AccountDto updateAccount(Long userId, String accNo, String name, String type, String status)
            throws Exception {
        String res = invokeTxn("UpdateAccount", String.valueOf(userId), accNo, name, type, status);
        return parseTAccountDto(res);
    }

    @Override
    public AccountDto readAccount(String accNo) throws Exception {
        String res = invokeQuery("ReadAccount", accNo);
        return parseTAccountDto(res);
    }

    @Override
    public TransactionDto readTransaction(String transactionId) throws Exception {
        String res = invokeQuery("ReadTransaction", transactionId);
        return parseToTransactionDto(res);
    }

    @Override
    public TransactionDto[] readTransactionHistory(String accNo) throws Exception {
        String res = invokeQuery("ReadTransactionHistory", accNo);
        if (hasResponseData(res)) {
            return toTransactionArr(res);
        }

        return null;

    }

    @Override
    public AccountDto[] readUserAccounts(Long userId) throws Exception {
        String res = invokeQuery("ReadUserAccounts", String.valueOf(userId));
        if (hasResponseData(res)) {
            return toAccountArr(res);
        }

        return null;
    }

    @Override
    public TransactionDto transferFunds(Long senderUserId, String transactionId, String senderAccNo,
            String recieverAccNo,
            int amount, String narration, String timestamp) throws Exception {

        String res = invokeTxn("TransferFunds",
                String.valueOf(senderUserId), transactionId,
                senderAccNo, recieverAccNo, String.valueOf(amount),
                narration, timestamp);

        return parseToTransactionDto(res);
    }

    private AccountDto[] toAccountArr(String data) {
        try {
            return new ObjectMapper().readValue(data, AccountDto[].class);
        } catch (IOException e) {
            return null;
        }
    }

    private TransactionDto[] toTransactionArr(String data) {
        try {
            return new ObjectMapper().readValue(data, TransactionDto[].class);
        } catch (IOException e) {
            return null;
        }
    }

    private AccountDto toAccount(String data) {
        try {
            return new ObjectMapper().readValue(data, AccountDto.class);
        } catch (IOException e) {
            return null;
        }
    }

    private TransactionDto toTransaction(String data) {
        try {
            return new ObjectMapper().readValue(data, TransactionDto.class);
        } catch (Exception e) {
            return null;
        }
    }

    private TransactionDto parseToTransactionDto(String res) {
        if (hasResponseData(res)) {
            return toTransaction(res);
        }

        return null;
    }

    private AccountDto parseTAccountDto(String res) {
        if (hasResponseData(res)) {
            return toAccount(res);
        }

        return null;
    }

    private boolean hasResponseData(String data) {
        return data != null && data.length() > 0;
    }
}
