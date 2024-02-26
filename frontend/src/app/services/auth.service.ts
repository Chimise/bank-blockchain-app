import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>('/authenticate', { username, password })
      .pipe(
        catchError((err) => {
          console.error(err);
          return throwError(err);
        })
      );
  }

  isLoggedIn(): boolean {
    // Check for a valid token in local storage (e.g., localStorage.getItem('token'))
    const token = localStorage.getItem('token');
    return !!token; // Return true if token exists and is not null or empty
  }
}
