import { Object as DataType, Property } from 'fabric-contract-api';
import { assertValue } from "../utils";
import { Currency } from './transaction';
import { DocType } from './doctype';

export enum AccountType {
    Saving = "Saving",
    Current = "Current",
    Fixed = "Fixed"
}

export enum AccountStatus {
    Active = "active",
    InActive = "inactive",
    Closed = "closed",
    Frozen = "frozen"
}

export enum TransactionLimit {
    Saving = 10000000,
    Current = 50000000,
    Fixed = 50000000
}

const DAILY_LIMIT = 100000000;



@DataType()
export class Account {
    @Property()
    public accNo: string;

    @Property()
    public name: string;

    @Property()
    public docType: string = DocType.Account;

    @Property()
    public type: string;

    @Property()
    public userId: number;

    @Property()
    public bal: number;

    @Property()
    public status: string = AccountStatus.Active;

    @Property()
    public createdAt: string;

    @Property()
    public updatedAt: string;

    @Property()
    public transactionLimit: number;

    @Property()
    public dailyLimit: number;

    @Property()
    public currency: string;

    constructor(accNo: string, accName: string, userId: number, balance: number, accountType?: string) {
        this.accNo = accNo;
        this.name = accName;
        this.userId = userId;
        this.bal = balance ?? 0;
        this.currency = Currency.NGN;
        this.type = accountType ?? AccountType.Saving;
        this.transactionLimit = TransactionLimit[this.type];
        this.createdAt = new Date().toISOString();
        this.updatedAt = new Date().toISOString();
        this.dailyLimit = DAILY_LIMIT;
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
        const balance = assertValue(state.bal, "Balance is required");

        const account = new Account(accNo, name, userId, balance);
        Object.assign(account, state);

        return account;
    }

    public toObject(): Account {
        return {
            ...this
        };
    }
}


