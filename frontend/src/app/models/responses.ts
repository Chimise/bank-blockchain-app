export interface User {
    id: number;
    email: string;
    isAdmin: boolean;
    firstName: string;
    lastName: string;
    username: string;
    presentAddress: string;
    permanentAddress: string;
    city: string;
    country: string;
    postalCode: string;
    phoneNumber: string;
}

export interface Account {
    accNo: string;
    name: string;
    docType: string;
    type: string;
    userId: number;
    bal: number;
    status: string;
    createdAt: string;
    updatedAt: string;
    transactionLimit: number;
    dailyLimit: number;
    currency: string;
}


export interface Transaction {
    type: string;
    id: string;
    docType: string;
    from: string;
    amount: number;
    createdAt: string;
    description: string;
    to: string;
    currency: string;
    mode: string;
}

export interface Response<T> {
    status: string,
    payload: T,
    errors?: Errors
    metadata: any;
}


export interface Errors {
    message: string;
    details: string;
}