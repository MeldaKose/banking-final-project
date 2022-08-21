import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router'

import { AppComponent } from './app.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { AccountsComponent } from './components/accounts/accounts.component';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { LoginGuard } from './guards/login.guard';
import { TransferComponent } from './components/transfer/transfer.component';
import { ToastrModule } from 'ngx-toastr';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    AccountsComponent,
    TransferComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      positionClass:"toast-bottom-right"
    }),
    RouterModule.forRoot([
      {path: 'register', component: RegisterComponent},
      {path: 'login', component: LoginComponent},
      {path: '', redirectTo: '/login', pathMatch: 'full'},
      {path: 'accounts', component: AccountsComponent, canActivate:[LoginGuard]},
      {path: 'transfer', component: TransferComponent, canActivate:[LoginGuard]},
    ]),
  ],
  providers: [
    {provide:HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi:true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
