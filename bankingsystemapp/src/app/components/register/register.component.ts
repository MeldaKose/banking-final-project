import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { RegisterErrorResponse } from 'src/app/models/RegisterErrorResponse';
import { RegisterSuccess } from 'src/app/models/registersuccess';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(private authService: AuthService,private router: Router,private toastrService:ToastrService){}

  ngOnInit(): void {
  }
  public onAddUser(addForm: NgForm): void {
    this.authService.register(addForm.value).subscribe(
      (response: RegisterSuccess) => {
        console.log(response);
        this.toastrService.success(response.message);
        addForm.reset();
        this.router.navigate(['/login']);
      },
      (error: RegisterErrorResponse) => {
        this.toastrService.error(error.message);
        addForm.reset();
      }
    );
  }

}
