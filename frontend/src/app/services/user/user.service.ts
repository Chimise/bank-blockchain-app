import { Injectable } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { resolve } from '../../utils';
import { HttpClient } from '@angular/common/http';
import { Response, User } from '../../models/responses';

@Injectable({
  providedIn: 'root'
})
export class UserService {


  constructor(
    private authService: AuthService,
    private httpClient: HttpClient
  ) { }

  getUser(): Promise<User> {
    return new Promise((res, rej) => {
      let user = this.authService.getUser();

      if (user) {
        return res(user);
      }

      this.httpClient.get<Response<User>>(resolve("/api/profile"), {
        headers: this.authService.getHeaders()
      }).subscribe({
        next(response) {
          user = response.payload;
          res(user)
        },
        error(err) {
          rej(err);
        }
      })
    }
    )


  }
}
