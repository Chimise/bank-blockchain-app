import { Object as DataType, Property } from "fabric-contract-api";
import { assertValue } from "../utils";

export enum TransactionType {
    Credit = "CR",
    Debit = "DR"
}

export enum TransactionStatus {
    Pending = "pending",
    Completed = "completed",
    Failure = "failure"
}

export enum TransactionMethod {
    Online = "online",
    Atm = "atm",
    InStore = "in-store"
}

export enum Currency {
    NGN = "NGN",
    USD = "USD"
}

@DataType()
export class Transaction {
    @Property("id", "string")
    public id: string;

    @Property("accNo", "string")
    public accNo: string;

    @Property("type", "string")
    public type: TransactionType;

    @Property("amount", "number")
    public amount: number;

    @Property("createdAt", "string")
    public createdAt: string;

    @Property("description", "string")
    public description: string = "";

    @Property("receiver", "string")
    public receiver: string = "";

    @Property("currency", "string")
    public currency: Currency = Currency.NGN;
    @Property("fee", "number")
    public fee: number = 0;

    constructor(id: string, accNo: string, type: TransactionType, amount: number, createdAt?: string) {
        this.id = id;
        this.accNo = accNo;
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt ? createdAt : new Date().toISOString();
    }

    public setReceiver(receiver: string) {
        this.receiver = receiver;
    }

    public setCurrency(currency: Currency) {
        this.currency = currency;
    }

    public setDescription(description: string) {
        this.description = description;
    }

    public setFee(fee: number) {
        this.fee = fee;
    }

    public static create(state: Partial<Transaction>): Transaction {
        const id = assertValue(state.id, 'Must provide a transaction id');
        const accNo = assertValue(state.accNo, "Acc No must be provided");
        const type = assertValue(state.type, "Transaction type must be provided");
        const amount = assertValue(state.amount, "Transaction amount must be provided");

        const transaction = new Transaction(id, accNo, type, amount);
        if (state.fee) {
            transaction.setFee(state.fee);
        }

        if (state.receiver) {
            transaction.setReceiver(state.receiver);
        }

        if (state.description) {
            transaction.setDescription(state.description);
        }

        if (state.currency) {
            transaction.setCurrency(state.currency);
        }

        return transaction;
    }

}

