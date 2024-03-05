import { Object as DataType, Property } from "fabric-contract-api";
import { DocType } from "./doctype";

export enum AllBankAccounts {
    CHARGES_ACCOUNT = "BANK101",
    DEPOSIT_ACCOUNT = "BANK102"
}

@DataType()
export class BankAccount {
    @Property()
    accNo: string;

    @Property()
    bal: number;

    @Property()
    name: string;

    @Property()
    docType: string = DocType.BankAccount;

    @Property()
    createdAt: string;

    constructor(accNo: string, bal: number, name: string) {
        this.accNo = accNo;
        this.bal = bal;
        this.name = name;
        this.createdAt = new Date().toISOString();
    }

    static create(account: BankAccount) {
        const bankAcc = new BankAccount(account.accNo, account.bal, account.name);
        bankAcc.createdAt = account.createdAt;
        return bankAcc;
    }

    public addFund(fund: number) {
        this.bal += fund;
    }

    public toObject(): BankAccount {
        return { ...this };
    }
}
