package com.firstacademy.firstblock.service;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firstacademy.firstblock.dto.model.AccountDto;

import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.Contract;

@Service
public class BlockchainServiceImpl implements BlockchainService {

    @Autowired
    private Gateway gateway;

    @Override
    public String invokeQuery(String functionName, String... args) throws Exception {
        // ... existing code ...
    }

    @Override
    public void invokeTxn(String functionName, String... args) throws Exception {
        // ... existing code ...
    }

    @Override
    public String initLedger() throws Exception {
        return invokeTxn("InitLedger");
    }

    @Override
    public void createAccount(int userId, String accNo, String accountName, int initialBalance) throws Exception {
        invokeTxn("CreateAccount", String.valueOf(userId), accNo, accountName, String.valueOf(initialBalance));
    }

    @Override
    public boolean accountExists(String accNo) throws Exception {
        String response = invokeQuery("AccountExists", accNo);
        return Boolean.parseBoolean(response);
    }

    @Override
    public void updateAccount(int userId, String accNo, String name, String type, String status) throws Exception {
        invokeTxn("UpdateAccount", String.valueOf(userId), accNo, name, type, status);
    }

    @Override
    public String readAccount(String accNo) throws Exception {
        return invokeQuery("ReadAccount", accNo);
    }

    @Override
    public void transferFunds(int senderUserId, String transactionId, String senderAccNo, String recieverAccNo, int amount, String narration, String timestamp) throws Exception {
        invokeTxn("TransferFunds", String.valueOf(senderUserId), transactionId, senderAccNo, recieverAccNo, String.valueOf(amount), narration, timestamp);
    }

    @Override
    public String readTransaction(String transactionId) throws Exception {
        return invokeQuery("ReadTransaction", transactionId);
    }

    @Override
    public String readTransactionHistory(String accNo) throws Exception {
        return invokeQuery("ReadTransactionHistory", accNo);
    }
}
