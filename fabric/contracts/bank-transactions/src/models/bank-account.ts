import { Object as DataType, Property } from "fabric-contract-api";

export enum BankAccountType {
    Deposits = "deposits",

}


@DataType()
export class BankAccount {
    @Property("accNo", "number")
    accNo: number;

    @Property("bal", "number")
    bal: number;

    @Property("name", "string")
    name: string;

}
