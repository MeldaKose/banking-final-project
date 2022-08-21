import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MyUser } from './models/myuser';
import { RegisterSuccess } from './models/registersuccess';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  
  title = 'bankingsystemapp';
  constructor(private authService: AuthService){}
  ngOnInit(): void {
    
  }

  public onAddEmloyee(addForm: NgForm): void {
    this.authService.register(addForm.value).subscribe(
      (response: RegisterSuccess) => {
        console.log(response);
        alert(response);
        addForm.reset();
      },
      (error: HttpErrorResponse) => {
        alert(error.message+"aa");
        addForm.reset();
      }
    );
  }
}
