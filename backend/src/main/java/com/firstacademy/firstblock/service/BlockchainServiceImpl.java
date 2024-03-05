package com.firstacademy.firstblock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.Network;
import org.hyperledger.fabric.client.Transaction;
import org.hyperledger.fabric.client.EventHub;
import org.hyperledger.fabric.client.EventListener;


@Service
public class BlockchainServiceImpl implements BlockchainService {

    @Autowired
    private Contract contract;
    private Gateway gateway;

    private String channelName = "OUR_CHANNEL";

    private Network getNetwork() throws Exception {
        return gateway.getNetwork(channelName);
    }

    @Override
    public String invokeQuery(String functionName, String... args) throws Exception {
        try {
            byte[] responseBytes = contract.evaluateTransaction(functionName, args);
            return new String(responseBytes);
        } catch (Exception e) {
            throw new Exception("Query failed: " + e.getMessage());
        }
    }

    @Override
    public String invokeTxn(String functionName, String... args) throws Exception {
        try {
            contract.submitTransaction(functionName, args);
            return "Transaction Submitted Successfully!";
        } catch (Exception e) {
            throw new Exception("Transaction failed: " + e.getMessage());
        }
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
    public String readTransaction(String transactionId) throws Exception {
        return invokeQuery("ReadTransaction", transactionId);
    }

    @Override
    public String readTransactionHistory(String accNo) throws Exception {
        return invokeQuery("ReadTransactionHistory", accNo);
    }

    @Override
    public String readUserAccounts(int userId) throws Exception {
        return invokeQuery("ReadUserAccounts", String.valueOf(userId));
    }

    private void registerTransferFundsEventListener(Transaction transaction) {
        
        Network network = getNetwork();

        EventHub eventHub = network.getEventHub();
        
        String eventName = "TransferFundsEvent";
        EventListener eventListener = event -> {
            System.out.println("Transfer event received: " + event.payloadAsString());
        };
        eventHub.registerEventListener(eventName, eventListener);
    }

    @Override
    public void transferFunds(int senderUserId, String transactionId, String senderAccNo, String recieverAccNo,
            int amount, String narration, String timestamp) throws Exception {

        contract.submitTransaction("TransferFunds",
                String.valueOf(senderUserId), transactionId,
                senderAccNo, recieverAccNo, String.valueOf(amount),
                narration, timestamp);
    }

}
