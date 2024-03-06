import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { FooterComponent } from '../../components/footer/footer.component';
import { HeaderComponent } from '../../components/header/header.component';

import {
  ReactiveFormsModule,
  FormControl,
  FormGroup,
  Validators,
  FormBuilder,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,

  imports: [HeaderComponent, FooterComponent, ReactiveFormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit, OnDestroy {
  private authSubscription!: Subscription;

  constructor(private authService: AuthService, private router: Router, private formBuilder: FormBuilder) {}

  loginForm: FormGroup = new FormGroup({});

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    })


    // this.loginForm = new FormGroup({
    //   username: new FormControl('', [Validators.required]),
    //   password: new FormControl('', Validators.minLength(3)),
    // });
  }


  onSubmit() {
    if (this.loginForm.valid){
      console.log('valid')

    }
  }

  // public loginForm!: FormGroup<{
  //   username: FormControl<string | null>;
  //   password: FormControl<string | null>;
  // }>;



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
      .login(values.username!, values.password!)
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
