import stringify from "json-stringify-deterministic";
import sortKeysRecursive from "sort-keys-recursive";
import { Account } from "../models";
import { AccountType } from "../models/account";

const utf8Decoder = new TextDecoder();

export function assertValue<T>(value: T | undefined | null, message: string): T {
    if (!value || typeof value === 'string' && value.length === 0) {
        throw new Error(message);
    }

    return value;
}

export function marshal(o: object): Buffer {
    return Buffer.from(toJSON(o), "utf-8");
}

export function toJSON(o: object): string {
    // Insert data in alphabetic order using 'json-stringify-deterministic' and 'sort-keys-recursive'
    return stringify(sortKeysRecursive(o));
}


export function unmarshal<T = object>(bytes: Uint8Array | string): T {
    const json = typeof bytes === 'string' ? bytes : utf8Decoder.decode(bytes);
    const parsed: unknown = JSON.parse(json);
    if (parsed === null || typeof parsed !== 'object') {
        throw new Error(`Invalid JSON type (${typeof parsed}): ${json}`);
    }

    return parsed as T;
}

export function ignoreNullable<T>(obj: T): T {
    return Object.entries(obj).reduce((newObj, [key, value]) => {
        if (value !== undefined) {
            newObj[key] = value;
        }

        return newObj;
    }, {} as T)
}

export function mergeObjects<T, U>(target: T, source: U): T & U {
    source = ignoreNullable(source);
    return Object.assign({}, target, source);
}

export function computeCharge(account: Account, amount: number) {
    if (account.type === AccountType.Current) {
        return 0;
    }

    return 10 * 100;
}

export function getDateFromISO(isoDate: string) {
    return isoDate.split("T")[0];
}