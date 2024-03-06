import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { resolve } from '../../utils/index';
import { Response, User } from '../../models/responses';
import { Router } from '@angular/router';

const TOKEN_KEY = "x-auth-token";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private token?: string;
  private user?: User;

  constructor(private httpClient: HttpClient,
    private router: Router) { }

  public isAuthenticated(): Promise<boolean> {
    const response = this.httpClient.get<Response<User>>(resolve("/api/profile"), { headers: this.getHeaders() });
    return new Promise((res, rej) => {
      const subscriber = response.subscribe({
        next: (response) => {
          this.user = response.payload;
          res(true)
        },
        error() {
          rej(false)
        },
        complete() {
          subscriber.unsubscribe()
        }
      })
    })
  }

  public getUser() {
    if(this.user) {
      return this.user;
    }

    return null;
  }

  public login(email: string, password: string) {
    const response = this.httpClient.post<Response<String>>(resolve("/api/auth/login"), { email, password }, {
      withCredentials: true,
      headers: this.getHeaders(false)
    });

    return response;
  }

  public logout() {

    this.token = undefined;
    sessionStorage.removeItem(TOKEN_KEY)
    this.router.navigate(['/login'])
  }


  public setToken(token: string) {
    this.token = token;
    sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getHeaders(includeAuth: boolean = true) {
    const headers: Record<string, any> = {}
    const token = this.getToken();
    if (token && includeAuth) {
      headers["Authorization"] = `Bearer ${token}`;
    }

    headers["Content-Type"] = "application/json";
    return headers;
  }

  public getToken(): string | null {
    let token = this.token ? this.token : null;
    if (!token) {
      token = sessionStorage.getItem(TOKEN_KEY);
    }

    return token;
  }

}
