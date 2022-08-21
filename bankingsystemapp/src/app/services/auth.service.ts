import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { MyUser } from '../models/myuser';
import { RegisterRequest } from '../models/registerrequest';
import { RegisterSuccess } from '../models/registersuccess';
import { LoginRequest } from '../models/loginrequest';
import { LoginSuccess } from '../models/loginsuccess';

@Injectable({
    providedIn: 'root'
  })
  export class AuthService {
  
    apiUrl = 'http://localhost:8080';
    constructor(private httpClient:HttpClient) { }
  
    public register(request: RegisterRequest): Observable<RegisterSuccess> {
        return this.httpClient.post<RegisterSuccess>(`${this.apiUrl}/register`, request);
      }
    
      public login(request: LoginRequest): Observable<LoginSuccess> {
        return this.httpClient.post<LoginSuccess>(`${this.apiUrl}/auth`, request);
      }

      public isAuthenticated(){
        if(localStorage.getItem("token")){
          return true;
        }
        else{
          return false;
        }
      }
  
  }