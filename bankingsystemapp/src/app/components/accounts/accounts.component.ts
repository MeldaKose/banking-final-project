import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccountWithBank } from 'src/app/models/AccountWithBank';
import { Bank } from 'src/app/models/bank';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {
  public accounts: AccountWithBank[];

  constructor(private accountService: AccountService,private router: Router) {
    this.accounts = [];
   }

  ngOnInit(): void {
    this.getAccounts();
  }
  public getAccounts(): void {
    this.accountService.getAccounts().subscribe(
      (response: AccountWithBank[]) => {
        this.accounts = response;
        console.log(this.accounts);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

}
