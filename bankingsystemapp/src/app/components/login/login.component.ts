import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoginSuccess } from 'src/app/models/loginsuccess';
import { MyUser } from 'src/app/models/myuser';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private authService: AuthService,private router: Router, private toastrService:ToastrService){}

  ngOnInit(): void {
  }

  public onlogin(loginForm: NgForm): void {
    this.authService.login(loginForm.value).subscribe(
      (response: LoginSuccess) => {
        localStorage.setItem("token",response.token);
        this.toastrService.success(response.message);
        this.router.navigate(['/accounts']);
      },
      (error: HttpErrorResponse) => {
        this.toastrService.error("Bad credentials");
        loginForm.reset();
      }
    );
  }

}
