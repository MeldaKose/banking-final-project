import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Account } from 'src/app/models/account';
import { AccountTransferSuccessResponse } from 'src/app/models/AccountTransferSuccessResponse';
import { AccountWithBank } from 'src/app/models/AccountWithBank';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-transfer',
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.css']
})
export class TransferComponent implements OnInit {

  public accounts: AccountWithBank[];
  
  constructor(private accountService: AccountService,private router: Router,private toastrService:ToastrService) { 
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
  public onTransfer(transferForm: NgForm): void {
    this.accountService.getByAccountnumber(transferForm.value.receiverAccountId).subscribe(
      (response: Account) => {
        transferForm.value.receiverAccountId = response.id;
        console.log(transferForm.value.receiverAccountId);
        this.accountService.transfer(transferForm.value,transferForm.value.senderAccountId).subscribe(
          (response: AccountTransferSuccessResponse) => {
            console.log(response);
            this.toastrService.success(response.message);
            transferForm.reset();
          },
          (error: HttpErrorResponse) => {
            this.toastrService.error(error.message);
            transferForm.reset();
          }
        );
      },
      (error: HttpErrorResponse) => {
        this.toastrService.error(error.message);
      }

    );
   
  }

}
