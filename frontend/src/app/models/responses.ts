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