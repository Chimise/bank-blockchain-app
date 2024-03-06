package com.firstacademy.firstblock.service;

import com.firstacademy.firstblock.dto.model.AccountDto;
import com.firstacademy.firstblock.dto.model.TransactionDto;

public interface BlockchainService {

    String invokeQuery(String functionName, String... args) throws Exception;

    String invokeTxn(String functionName, String... args) throws Exception;

    String initLedger() throws Exception;

    AccountDto createAccount(Long userId, String accNo, String accountName, int initialBalance) throws Exception;

    boolean accountExists(String accNo) throws Exception;

    AccountDto updateAccount(Long userId, String accNo, String name, String type, String status) throws Exception;

    AccountDto readAccount(String accNo) throws Exception;

    TransactionDto transferFunds(Long senderUserId, String transactionId, String senderAccNo, String recieverAccNo,
            int amount,
            String narration, String timestamp) throws Exception;

    TransactionDto readTransaction(String transactionId) throws Exception;

    TransactionDto[] readTransactionHistory(String accNo) throws Exception;

    AccountDto[] readUserAccounts(Long userId) throws Exception;

}
