import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { FooterComponent } from '../../components/footer/footer.component';
import { HeaderComponent } from '../../components/header/header.component';

import {
  ReactiveFormsModule,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,

  imports: [HeaderComponent, FooterComponent, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit, OnDestroy {
  private authSubscription!: Subscription;

  constructor(private authService: AuthService, private router: Router) {}

  public loginForm!: FormGroup<{
    email: FormControl<string | null>;
    password: FormControl<string | null>;
  }>;

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.minLength(3)),
    });
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  handleSubmit() {
    const values = this.loginForm.value;
    console.log(values);
    this.authSubscription = this.authService
      .login(values.email!, values.password!)
      .subscribe({
        next: (body) => {
          console.log(body);
          const token = body.metadata as string;
          console.log(token);
          if (token) {
            this.authService.setToken(token);
            console.log('Logged In successfully');
            this.router.navigate(['/dashboard']);
          }
        },
        error() {
          console.log('An error occurred, could not log in');
        },
      });
  }
}
