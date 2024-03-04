import { Object, Property } from 'fabric-contract-api';

@Object()
export class Account {
    @Property()
    public docType?: string;

    @Property()
    public ID: string;

    @Property()
    public Name: string;

    @Property()
    public Balance: number;

    @Property()
    public Currency: string;

    @Property()
    public AccountNumber: number;
}