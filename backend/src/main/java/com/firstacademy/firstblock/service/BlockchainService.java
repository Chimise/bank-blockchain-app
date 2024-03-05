package com.firstacademy.firstblock.service;

public interface BlockchainService {

    String invokeQuery(String functionName, String... args) throws Exception;

    String invokeTxn(String functionName, String... args) throws Exception;

    String initLedger() throws Exception;

    void createAccount(int userId, String accNo, String accountName, int initialBalance) throws Exception;

    boolean accountExists(String accNo) throws Exception;

    void updateAccount(int userId, String accNo, String name, String type, String status) throws Exception;

    String readAccount(String accNo) throws Exception;

    void transferFunds(int senderUserId, String transactionId, String senderAccNo, String recieverAccNo, int amount, String narration, String timestamp) throws Exception;

    String readTransaction(String transactionId) throws Exception;

    String readTransactionHistory(String accNo) throws Exception;
    
    String readUserAccounts(int userId) throws Exception;

}
