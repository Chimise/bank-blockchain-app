package com.firstacademy.firstblock.service;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.Contract;

@Service
public class BlockchainServiceImpl implements BlockchainService {

    @Autowired
    private Gateway gateway;

    @Override
    public String invokeQuery(String functionName, String... args) throws Exception {
    
    }

    @Override
    public String invokeTxn(String functionName, String... args) throws Exception {
        
    }


    @Override
    public String initLedger() throws Exception {
        return invokeTxn("InitLedger");
    }

    @Override
    public Account createAccount(int userId, String accNo, String accountName, int initialBalance) throws Exception {
        String response = invokeTxn("CreateAccount", String.valueOf(userId), accNo, accountName, String.valueOf(initialBalance));
        // Parse response to Account object
        return parseAccount(response);
    }

    @Override
    public boolean accountExists(String accNo) throws Exception {
        String response = invokeQuery("AccountExists", accNo);
        return Boolean.parseBoolean(response);
    }

    @Override
    public Account updateAccount(int userId, String accNo, String name, String type, String status) throws Exception {
        String response = invokeTxn("UpdateAccount", String.valueOf(userId), accNo, name, type, status);
        // Parse response to Account object
        return parseAccount(response);
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

    private Account parseAccount(String response) throws Exception {
        // Implement logic to parse the JSON response and create an Account object
        // Use a library like Gson or Jackson for better parsing
        throw new UnsupportedOperationException("Account parsing not implemented yet");
    }
}
