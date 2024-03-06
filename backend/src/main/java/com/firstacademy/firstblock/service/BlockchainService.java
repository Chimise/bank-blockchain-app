package com.firstacademy.firstblock.service;

import com.firstacademy.firstblock.dto.model.AccountDto;

public interface BlockchainService {

    String invokeQuery(String functionName, String... args) throws Exception;

    String invokeTxn(String functionName, String... args) throws Exception;

    String initLedger() throws Exception;

    AccountDto createAccount(Long userId, String accNo, String accountName, int initialBalance) throws Exception;

    boolean accountExists(String accNo) throws Exception;

    void updateAccount(Long userId, String accNo, String name, String type, String status) throws Exception;

    String readAccount(String accNo) throws Exception;

    String transferFunds(Long senderUserId, String transactionId, String senderAccNo, String recieverAccNo, int amount,
            String narration, String timestamp) throws Exception;

    String readTransaction(String transactionId) throws Exception;

    String readTransactionHistory(String accNo) throws Exception;

    String readUserAccounts(Long userId) throws Exception;

}
