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
    @Property("accNo", "string")
    public accNo: string;

    @Property("name", "string")
    public name: string;

    @Property("docType", "string")
    public docType = DocType.Account

    @Property("type", "string")
    public type: AccountType;

    @Property("userId", "number")
    public userId: number;

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

    constructor(accNo: string, accName: string, userId: number, curr?: Currency, accountType?: AccountType) {
        this.accNo = accNo;
        this.name = accName;
        this.userId = userId;
        this.currency = curr ? curr : Currency.NGN;
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

        const account = new Account(accNo, name, userId);
        Object.assign(account, state);

        return account;
    }
}


