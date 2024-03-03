import { environment } from "../../environments/environment";

export const resolve = (baseUrl: string) => {
    return `${environment.apiUrl}${baseUrl}`;
}