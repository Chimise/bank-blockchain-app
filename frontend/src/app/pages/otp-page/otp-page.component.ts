import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-otp-page',
  standalone: true,
  imports: [FormsModule,
  ReactiveFormsModule
],
  templateUrl: './otp-page.component.html',
  styleUrl: './otp-page.component.css'
})
export class OtpPageComponent implements OnInit {
  otpInput: FormGroup = new FormGroup({});

  constructor(private otpForm: FormBuilder){

  }
  ngOnInit(): void {
    this.otpInput = this.otpForm.group({

      input1: ['', Validators.required],
      input2: ['', Validators.required],
      input3: ['', Validators.required],
      input4: ['', Validators.required],
      input5: ['', Validators.required],

    })
  }

  onInputChange(event: any) {
    const inputValue = event.target.value;
    if (inputValue.length > 1) {
        event.target.value = inputValue.slice(0, 1); 
    }
}
}
