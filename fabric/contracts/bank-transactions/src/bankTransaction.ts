import { Context, Contract, Info, Returns, Transaction } from 'fabric-contract-api';
import { unmarshal, marshal, toJSON, mergeObjects, computeCharge, getDateFromISO } from './utils';

import { BankAccount, Transaction as BankTransaction, Account, DocType } from './models';
import { AccountStatus } from './models/account';
import { TransactionMode, TransactionType } from './models/transaction';
import { AllBankAccounts } from './models/bank-account';


@Info({ title: "Bank Transfer", description: "Smart Contract for handling internal bank transfers" })
export class BankTransactionContract extends Contract {

    @Transaction()
    public async InitLedger(ctx: Context): Promise<void> {
        const bankSavingAcc = new BankAccount(AllBankAccounts.CHARGES_ACCOUNT, 300000000, "Deposits");

        await ctx.stub.putState(bankSavingAcc.accNo, marshal(bankSavingAcc.toObject()));

        this.#logger(ctx).info("Initialized the bank saving account");
    }

    @Transaction()
    public async CreateAccount(ctx: Context, userId: number, accNo: string, accountName: string, initialBalance: number): Promise<void> {
        const acc = new Account(accNo, accountName, userId, initialBalance);
        const accExists = await this.AccountExists(ctx, acc.accNo)
        if (accExists) {
            throw new Error(`Account no ${acc} already exists`);
        }

        const accBytes = marshal(acc.toObject());
        await ctx.stub.putState(acc.accNo, accBytes);
        await ctx.stub.setEvent("CreateAccount", accBytes);
        this.#logger(ctx).info(`Account ${acc.accNo} was created`);
    }

    @Transaction(false)
    @Returns("boolean")
    public async AccountExists(ctx: Context, accNo: string): Promise<boolean> {
        const accountBytes = await ctx.stub.getState(accNo);
        return accountBytes && accountBytes.length > 0;
    }

    @Transaction()
    public async UpdateAccount(ctx: Context, userId: number, accNo: string, name?: string, type?: string, status?: string): Promise<void> {

        if (arguments.length <= 1) {
            throw new Error("Update arguments is empty");
        }
        const updates: Partial<Account> = { name, status, type };

        const account = await this.#readAccount(ctx, accNo);

        if (account.userId !== userId) {
            throw new Error(`Account with no ${accNo} does not belong to this user ${userId}`)
        }

        const updatedAccount = mergeObjects(account, updates);
        const updatedAccountBytes = marshal(updatedAccount);

        return await ctx.stub.putState(accNo, updatedAccountBytes);
    }

    @Transaction(false)
    @Returns("string")
    public async ReadAccount(ctx: Context, accNo: string): Promise<string> {
        const account = await this.#readAccount(ctx, accNo);
        return JSON.stringify(account);
    }

    @Transaction()
    public async TransferFunds(ctx: Context, senderUserId: number, transactionId: string, senderAccNo: string, recieverAccNo: string, amount: number, narration: string, timestamp: string): Promise<void> {
        const senderAcc = await this.#readAccount(ctx, senderAccNo);
        const recieverAcc = await this.#readAccount(ctx, recieverAccNo);
        const bankAccc = await this.#readBankAccount(ctx, AllBankAccounts.CHARGES_ACCOUNT);

        if (senderAcc.userId !== senderUserId) {
            throw new Error(`User ${senderUserId} do not have adequate permissions`);
        }

        if (senderAcc.status !== AccountStatus.Active) {
            throw new Error(`Account ${senderAcc.accNo} is not currently active`);
        }

        if (recieverAcc.status !== AccountStatus.Active) {
            throw new Error(`Account ${recieverAcc.accNo} is not currently active`);
        }

        if (amount > senderAcc.transactionLimit) {
            throw new Error(`Account ${senderAccNo} has exceeded its transaction limit, please contact the bank `);
        }

        const transactionIdExists = await this.#transactionIdExists(ctx, transactionId);

        if (transactionIdExists) {
            throw new Error(`Transaction ${transactionId} has been previously created`);
        }

        const charge = computeCharge(senderAcc, amount);

        if (!hasEnoughFunds(senderAcc, amount, charge)) {
            throw new Error(`Insufficient Funds`);
        }

        if (await this.#isDailyLimitExceeded(ctx, senderAccNo, timestamp, senderAcc.dailyLimit)) {
            throw new Error(`Account ${senderAcc} has exceeded it's daily limit`);
        }

        const transaction = new BankTransaction(transactionId, senderAccNo, recieverAccNo, amount, timestamp);

        transaction.setMode(TransactionMode.Transfer);
        transaction.setDescription(narration);

        const chargeTransactionId = this.#generateChargeTransactionId(transactionId);

        const chargeTransaction = new BankTransaction(chargeTransactionId, senderAccNo, AllBankAccounts.CHARGES_ACCOUNT, charge, timestamp);
        chargeTransaction.setMode(TransactionMode.Charge);

        senderAcc.setBalance(senderAcc.bal - (amount + charge));
        recieverAcc.setBalance(recieverAcc.bal + amount);
        bankAccc.addFund(charge);

        try {
            const key = await this.#generateTransactionKey(ctx, transactionId, senderAccNo, recieverAccNo, timestamp);
            await ctx.stub.putState(key, marshal(transaction.toObject()));

            const chargeKey = await this.#generateTransactionKey(ctx, chargeTransactionId, senderAccNo, AllBankAccounts.CHARGES_ACCOUNT, timestamp);
            await ctx.stub.putState(chargeKey, marshal(chargeTransaction.toObject()));
        } catch (error) {
            throw new Error(`Transaction ${transactionId} couldn't complete`);
        }


        try {
            await ctx.stub.putState(senderAccNo, marshal(senderAcc.toObject()));
            await ctx.stub.putState(recieverAccNo, marshal(recieverAcc.toObject()));
            await ctx.stub.putState(AllBankAccounts.CHARGES_ACCOUNT, marshal(bankAccc.toObject()));
        } catch (error) {
            throw new Error(`Transaction ${transactionId} couldn't complete`);
        }

        ctx.stub.setEvent('TransferFund', marshal(transaction.toObject()));
    }


    @Transaction(false)
    @Returns("string")
    public async ReadTransaction(ctx: Context, transactionId: string): Promise<string> {
        const transactionIterator = await ctx.stub.getStateByPartialCompositeKey(DocType.Transaction, [transactionId]);
        const transactionResult = await transactionIterator.next();
        if (!transactionResult.value?.value) {
            throw new Error(`Transaction ${transactionId} does not exist`);
        }

        await transactionIterator.close();

        return transactionResult.value.value.toString();
    }

    @Transaction(false)
    @Returns("string")
    public async ReadTransactionHistory(ctx: Context, accNo: string): Promise<string> {
        const transactionIterator = await ctx.stub.getStateByPartialCompositeKey(DocType.Transaction, [accNo]);

        let result = await transactionIterator.next();
        let results: BankTransaction[] = []
        while (!result.done) {
            const transaction = BankTransaction.create(unmarshal<BankTransaction>(result.value.value));
            if (transaction.from === accNo) {
                transaction.setAccountType(TransactionType.Debit);
            } else if (transaction.to === accNo) {
                transaction.setAccountType(TransactionType.Credit);
            }

            results.push(transaction);
            result = await transactionIterator.next();
        }

        await transactionIterator.close();
        return toJSON(results);
    }

    @Transaction(false)
    @Returns("string")
    public async ReadUserAccounts(ctx: Context, userId: number) {
        const iterator = await ctx.stub.getStateByRange('', '');
        const accounts: Account[] = [];
        let result = await iterator.next();
        while (!result.done) {
            const document = unmarshal<{ docType: DocType }>(result.value.value);
            if (document.docType === DocType.Account) {
                const account = Account.create(document);
                if (account.userId === userId) {
                    accounts.push(account);
                }
            }

            result = await iterator.next();
        }

        await iterator.close()
        return JSON.stringify(accounts);
    }


    async #isDailyLimitExceeded(ctx: Context, senderAccNo: string, timestamp: string, dailyLimit: number) {
        const date = getDateFromISO(timestamp);
        const transactionEntries = await ctx.stub.getStateByPartialCompositeKey(DocType.Transaction, [senderAccNo, date]);

        let transactionSum = 0;
        let result = await transactionEntries.next();
        while (!result.done) {
            const transaction = unmarshal<BankTransaction>(result.value.value);
            if (transaction.from === senderAccNo) {
                transactionSum += transaction.amount;
            }

            result = await transactionEntries.next();
        }

        await transactionEntries.close();

        return transactionSum > dailyLimit;
    }

    async #transactionIdExists(ctx: Context, transactionId: string) {
        const resultIterator = await ctx.stub.getStateByPartialCompositeKey(DocType.Transaction, [transactionId]);
        const result = await resultIterator.next();
        await resultIterator.close();
        if (result.value?.value) {
            return true;
        }

        return false;
    }

    #logger(ctx: Context) {
        return ctx.logging.getLogger();
    }

    async #readAccount(ctx: Context, accNo: string): Promise<Account> {
        const accountBytes = await ctx.stub.getState(accNo);
        if (!accountBytes || accountBytes.length === 0) {
            throw new Error(`Account ${accNo} has not been created`);
        }

        return Account.create(unmarshal<Account>(accountBytes));
    }

    async #readBankAccount(ctx: Context, bankAccNo: AllBankAccounts) {
        const bankAccBytes = await ctx.stub.getState(bankAccNo);
        if (!bankAccBytes || bankAccBytes.length === 0) {
            throw new Error(`Account ${bankAccNo} has not been created`);
        }

        return BankAccount.create(unmarshal<BankAccount>(bankAccBytes));

    }

    #generateChargeTransactionId(transactionId: string) {
        return `${transactionId}-charge`;
    }

    async #generateTransactionKey(ctx: Context, transactionId: string, senderAcc: string, recieverAcc: string, timestamp: string) {
        const date = getDateFromISO(timestamp);
        const key = await ctx.stub.createCompositeKey(DocType.Transaction, [transactionId, senderAcc, recieverAcc, date]);
        return key;
    }

}


function hasEnoughFunds(account: Account, amount: number, charge: number = 0) {
    const remainingBal = account.bal - (amount + charge);
    if (remainingBal > 1000) {
        return true;
    }

    return false;
}



