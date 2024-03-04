import { Context, Contract, Info, Returns, Transaction } from "fabric-contract-api";    
import stringify from "json-stringify-deterministic";
import sortKeysRecursive from "sort-keys-recursive";
import { Account } from "./account";


@Info({ title: 'BankTransactions', description: 'Smart contract for banking transactions'})
export class BankTransferContract extends Contract {

    @Transaction()
    public async InitLedger(ctx: Context): Promise<void> {
        const accounts: Account[] = [
            {
                ID: 'account1',
                Name: 'Chisom',
                Balance: 150000,
                Currency: 'NGN',
                AccountNumber: 1234567890,
            },
            {
                ID: 'account2',
                Name: 'Charles',
                Balance: 400000,
                Currency: 'NGN',
                AccountNumber: 1234567890,
            },
            {
                ID: 'account3',
                Name: 'Nasir',
                Balance: 230000,
                Currency: 'NGN',
                AccountNumber: 1234567890,
            },

        ];

        for (const account of accounts) {
            account.docType = 'account';
            await ctx.stub.putState(account.ID, Buffer.from(stringify(sortKeysRecursive(account))));
            console.info(`Account ${account.ID} initialized`);

        }
    }

    // CreateAccount creates a new account to the world state with given details.
    @Transaction()
    public async CreateAccount(ctx: Context, id: string, name: string, balance: number, currency: string, accountNumber: number): Promise<void> {
        const exists = await this.AccountExists(ctx, id);
        if (exists) {
            throw new Error(`The account ${id} already exists`);
        }

        const asset  = {
            ID: id,
            Name: name,
            Balance: balance,
            Currency: currency,
            AccountNumber: accountNumber,
        };
        // we insert data in alphabetic order using 'json-stringify-deterministic' and 'sort-keys-recursive'
        await ctx.stub.putState(id, Buffer.from(stringify(sortKeysRecursive(Account))));
    }


    // ReadAccount returns the account stored in the world state with given id.
    @Transaction(false)
    public async ReadAccount(ctx: Context, id: string): Promise<string> {
        const accountJson = await ctx.stub.getState(id); // get the account from chaincode state
        if (!accountJson || accountJson.length === 0) {
            throw new Error(`The account ${id} does not exist`);
        }
        return accountJson.toString();
    }

    // AccountExists returns true when account with given ID exists in world state.
    @Transaction(false)
    @Returns('boolean')
    public async AccountExists(ctx: Context, id: string): Promise<boolean> {
        const accountJSON = await ctx.stub.getState(id);
        return accountJSON && accountJSON.length > 0;
    }


}