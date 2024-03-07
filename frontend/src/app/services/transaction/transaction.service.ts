import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { resolve } from '../../utils';
import { AuthService } from '../auth/auth.service';
import { Transaction, Response, Account } from '../../models/responses';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(private httpClient: HttpClient, private authService: AuthService) { }

  public transfer(senderAccount: string, recieverAccount: string, amount: number, narration: string) {
    return this.httpClient.post<Response<Transaction>>(resolve("/api/transactions/transfer"), {
      senderAccountNumber: senderAccount,
      recieverAccountNumber: recieverAccount,
      amount,
      narration
    }, {
      headers: this.authService.getHeaders()
    })
  }

  public getAccountDetails(accNo: string) {
    return this.httpClient.get<Response<Account>>(resolve(`/api/accounts/${accNo}`), {
      headers: this.authService.getHeaders()
    });
  }

  public getTransactionHistory(accNo: string) {
    return this.httpClient.get<Response<Transaction[]>>(resolve(`/api/transactions/account/${accNo}`), { headers: this.authService.getHeaders() });
  }

}
