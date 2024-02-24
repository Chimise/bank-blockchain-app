import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [FormsModule],
})
export class LoginComponent implements OnInit {
  username!: string;
  password!: string;
  errorMessage!: string;

  constructor(private authService: AuthService, private router: Router, private httpClient: HttpClient) {}

  ngOnInit() {
    // Clear any existing error messages
    this.errorMessage = "";
  }

  onSubmit() {
    this.authService.login(this.username, this.password)
      .subscribe(
        (data: { token: string; }) => {
          // Store token in secure storage (e.g., local storage with encryption)
          localStorage.setItem('token', data.token);

          // Redirect to protected route
          this.router.navigate(['/']);
        },
        (error: { message: string; }) => {
          this.errorMessage = error.message; // Handle error message appropriately
        }
      );
  }
}
