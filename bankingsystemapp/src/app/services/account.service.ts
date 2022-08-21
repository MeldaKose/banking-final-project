import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Account } from "../models/account";
import { AccountTransferRequest } from "../models/AccountTransferRequest";
import { AccountTransferSuccessResponse } from "../models/AccountTransferSuccessResponse";
import { AccountWithBank } from "../models/AccountWithBank";

@Injectable({
    providedIn: 'root'
  })
  export class AccountService {
  
    apiUrl = 'http://localhost:8080';
    constructor(private httpClient:HttpClient) { }

    public getAccounts(): Observable<AccountWithBank[]> {
        return this.httpClient.get<AccountWithBank[]>(`${this.apiUrl}/accounts`);
      }
      public transfer(request: AccountTransferRequest,senderAccountId: number): Observable<AccountTransferSuccessResponse> {
        return this.httpClient.put<AccountTransferSuccessResponse>(`${this.apiUrl}/accounts/${senderAccountId}`, request);
      }

      public getByAccountnumber(number: number): Observable<Account> {
        return this.httpClient.get<Account>(`${this.apiUrl}/accounts/get/${number}`);
      }
      
  
}
    