import { Object as DataType, Property } from "fabric-contract-api";
import { assertValue } from "../utils";
import { DocType } from "./doctype";

export enum TransactionType {
    Credit = "CR",
    Debit = "DR"
}

export enum TransactionMethod {
    Online = "online",
    Atm = "atm",
    InStore = "in-store"
}

export enum TransactionMode {
    Transfer = "transfer",
    Charge = "charge"
}

export enum Currency {
    NGN = "NGN",
    USD = "USD"
}

@DataType()
export class Transaction {

    @Property()
    public type: string = null;

    @Property()
    public id: string;

    @Property()
    public docType: string = DocType.Transaction

    @Property()
    public from: string;

    @Property()
    public amount: number;

    @Property()
    public createdAt: string;

    @Property()
    public description: string = "";

    @Property()
    public to: string = "";

    @Property()
    public currency: string = Currency.NGN;

    @Property()
    public mode: string = TransactionMode.Transfer;

    constructor(id: string, from: string, to: string, amount: number, createdAt?: string) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.createdAt = createdAt ? createdAt : new Date().toISOString();
    }

    public setReceiver(to: string) {
        this.to = to;
    }

    public setCurrency(currency: Currency) {
        this.currency = currency;
    }

    public setDescription(description: string) {
        this.description = description;
    }

    public setAccountType(type: TransactionType) {
        this.type = type;
    }

    public setMode(mode: TransactionMode) {
        this.mode = mode;
    }

    public static create(state: Partial<Transaction>): Transaction {
        const id = assertValue(state.id, 'Must provide a transaction id');
        const senderAccNo = assertValue(state.from, "Sender Acc No must be provided");
        const amount = assertValue(state.amount, "Transaction amount must be provided");
        const receiverAccNo = assertValue(state.to, "Receiver Acc No must be provided");

        const transaction = new Transaction(id, senderAccNo, receiverAccNo, amount);
        Object.assign(transaction, state);
        return transaction;
    }

    public toObject(): Transaction {
        return { ...this };
    }
}

