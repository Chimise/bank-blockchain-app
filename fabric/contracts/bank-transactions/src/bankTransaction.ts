import { Context, Contract, Info, Returns, Transaction } from 'fabric-contract-api';
import { unmarshal, marshal, toJSON, mergeObjects, computeCharge, getDateFromISO } from './utils';

import { BankAccount, Transaction as BankTransaction, Account, DocType } from './models';
import { AccountStatus } from './models/account';
import { TransactionMode, TransactionType } from './models/transaction';
import { AllBankAccounts } from './models/bank-account';


@Info({ title: "Bank Transfer", description: "Smart Contract for handling internal bank transfers" })
export class BankTransactionContract extends Contract {

    @Transaction()
    public async InitLedger(ctx: Context) {
        const bankSavingAcc = new BankAccount(AllBankAccounts.CHARGES_ACCOUNT, 300000000, "Deposits");

        await ctx.stub.putState(bankSavingAcc.accNo, marshal(bankSavingAcc));

        this.#logger(ctx).info("Initialized the bank saving account");
    }

    @Transaction()
    public async CreateAccount(ctx: Context, state: Partial<Account>) {
        const acc = Account.create(state);
        const accExists = await this.AccountExists(ctx, acc.accNo)
        if (accExists) {
            throw new Error(`Account no ${acc} already exists`);
        }

        const accBytes = marshal(acc);
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
    public async UpdateAccount(ctx: Context, accNo: string, userId: number, state: Partial<Account>) {
        const updates: Partial<Account> = { name: state.name, type: state.type, status: state.status, dailyLimit: state.dailyLimit, transactionLimit: state.transactionLimit };

        const account = await this.#readAccount(ctx, accNo);

        if (account.userId !== userId) {
            throw new Error(`Account with no ${accNo} does not belong to this user ${userId}`)
        }

        const updatedAccount = mergeObjects(account, updates);
        const updatedAccountBytes = marshal(updatedAccount);
        ctx.stub.putState(accNo, updatedAccountBytes);
        ctx.stub.setEvent("UpdateAccount", updatedAccountBytes);
    }

    @Transaction(false)
    @Returns("Account")
    public async ReadAccount(ctx: Context, accNo: string) {
        const account = await this.#readAccount(ctx, accNo);
        return toJSON(account);
    }

    @Transaction()
    public async TransferFunds(ctx: Context, senderUserId: number, transactionId: string, senderAccNo: string, recieverAccNo: string, amount: number, narration: string) {
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
        const transactionDate = ctx.stub.getDateTimestamp().toISOString();

        if (await this.#isDailyLimitExceeded(ctx, senderAccNo, transactionDate, senderAcc.dailyLimit)) {
            throw new Error(`Account ${senderAcc} has exceeded it's daily limit`);
        }

        const transaction = new BankTransaction(transactionId, senderAccNo, recieverAccNo, amount, transactionDate);

        transaction.setMode(TransactionMode.Transfer);
        transaction.setDescription(narration);

        const chargeTransactionId = this.#generateChargeTransactionId(transactionId);

        const chargeTransaction = new BankTransaction(chargeTransactionId, senderAccNo, AllBankAccounts.CHARGES_ACCOUNT, charge, transactionDate);
        chargeTransaction.setMode(TransactionMode.Charge);

        senderAcc.setBalance(senderAcc.bal - (amount + charge));
        recieverAcc.setBalance(recieverAcc.bal + amount);
        bankAccc.addFund(charge);

        try {
            const key = await this.#generateTransactionKey(ctx, transactionId, senderAccNo, recieverAccNo, transactionDate);
            await ctx.stub.putState(key, marshal(transaction));

            const chargeKey = await this.#generateTransactionKey(ctx, chargeTransactionId, senderAccNo, AllBankAccounts.CHARGES_ACCOUNT, transactionDate);
            await ctx.stub.putState(chargeKey, marshal(chargeTransaction));
        } catch (error) {
            throw new Error(`Transaction ${transactionId} couldn't complete`);
        }


        try {
            await ctx.stub.putState(senderAccNo, marshal(senderAcc));
            await ctx.stub.putState(recieverAccNo, marshal(recieverAcc));
            await ctx.stub.putState(AllBankAccounts.CHARGES_ACCOUNT, marshal(bankAccc));
        } catch (error) {
            throw new Error(`Transaction ${transactionId} couldn't complete`);
        }

        ctx.stub.setEvent('TransferFund', marshal(transaction));
    }


    @Transaction(false)
    public async ReadTransaction(ctx: Context, transactionId: string) {
        const transactionIterator = await ctx.stub.getStateByPartialCompositeKey(DocType.Transaction, [transactionId]);
        const transactionResult = await transactionIterator.next();
        if (!transactionResult) {
            throw new Error(`Transaction ${transactionId} does not exist`);
        }

        await transactionIterator.close();

        return transactionResult.value.value.toString();
    }

    @Transaction(false)
    public async ReadTransactionHistory(ctx: Context, accNo: string) {
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
        if (result) {
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
    const remainingBal = (amount + charge) - account.bal;
    if (remainingBal > 1000) {
        return true;
    }

    return false;
}



