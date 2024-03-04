import { Injectable } from '@angular/core';
import { User } from '../../models/responses';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly url = '/api/profile';

  constructor(
    private httpClient: HttpClient 
  ) { }

  getUser(): Observable<User> {
    // const url = `${this.url}/${userId}`;
    return this.httpClient.get<User>(this.url);
  }
}
