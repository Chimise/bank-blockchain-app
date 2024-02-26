import { Object as DataType, Property } from 'fabric-contract-api';
import { assertValue } from "../utils";
import { Currency } from './transaction';

export enum AccountType {
    Saving = "saving",
    Current = "current",
    Fixed = "fixed-deposit"
}

export enum AccountStatus {
    Active = "active",
    InActive = "inactive",
    Closed = "closed",
    Frozen = "frozen"
}

export enum TransactionLimit {
    New = 10000,
    Saving = 20000,
    Current = 50000,
    Fixed = 500000
}



@DataType()
export class Account {
    @Property("accNo", "string")
    public accNo: string;

    @Property("name", "string")
    public name: string;

    @Property("userId", "string")
    public userId: string;

    @Property("bal", "number")
    public bal: number = 0;

    @Property("status", "string")
    public status: AccountStatus = AccountStatus.Active;

    @Property("createdAt", "string")
    public createdAt: string;

    @Property("updatedAt", "string")
    public updatedAt: string;

    @Property("transactionLimit", "number")
    public transactionLimit: number;

    @Property("dailyLimit", "number")
    public dailyLimit: number;

    @Property("currency", "string")
    public currency: Currency;

    constructor(accNo: string, accName: string, userId: string, curr?: Currency) {
        this.accNo = accNo;
        this.name = accName;
        this.userId = userId;
        this.currency = curr ? curr : Currency.NGN;
        this.createdAt = new Date().toISOString();
        this.updatedAt = new Date().toISOString();
        this.transactionLimit = TransactionLimit.New;
        this.dailyLimit = 200000;
    }

    public setBalance(bal: number) {
        this.bal = bal;
    }

    public setTransactionLimit(limit: number) {
        this.transactionLimit = limit;
    }

    public setDailyLimit(limit: number) {
        this.dailyLimit = limit;
    }

    public static create(state: Partial<Account>): Account {
        const accNo = assertValue(state.accNo, "Account Number is required");
        const name = assertValue(state.name, "Account Name is required");
        const userId = assertValue(state.userId, "User id is required");

        const account = new Account(accNo, name, userId, state.currency);
        if (state.bal) {
            account.setBalance(state.bal);
        }

        if (state.dailyLimit) {
            account.setDailyLimit(state.dailyLimit);
        }

        if (state.transactionLimit) {
            account.setTransactionLimit(state.transactionLimit);
        }

        return account;
    }



}


