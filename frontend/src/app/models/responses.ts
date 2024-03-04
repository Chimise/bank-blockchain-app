export interface User {
    id: number;
    email: string;
    isAdmin: boolean;
    firstName: string;
    lastName: string;
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