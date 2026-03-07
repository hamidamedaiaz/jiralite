import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService, UserResponse } from '../auth';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  username = '';
  email = '';
  password = '';

  successResponse: UserResponse | null = null;
  errorMessage: string | null = null;
  validationErrors: Record<string, string> = {};
  loading = false;

  constructor(private authService: AuthService) {}

  onSubmit() {
    this.loading = true;
    this.successResponse = null;
    this.errorMessage = null;
    this.validationErrors = {};

    this.authService.register({
      username: this.username,
      email: this.email,
      password: this.password,
    }).subscribe({
      next: (response) => {
        this.successResponse = response;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        if (err.status === 400) {
          this.validationErrors = err.error;
        } else if (err.status === 409) {
          this.errorMessage = err.error.error;
        } else {
          this.errorMessage = 'An unexpected error occurred. Please try again.';
        }
      }
    });
  }
}
