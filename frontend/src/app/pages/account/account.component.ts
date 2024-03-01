import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [FormsModule,
  ReactiveFormsModule
],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent implements OnInit{

  sendMoneyForm:FormGroup = new FormGroup({});

  constructor(private Fb: FormBuilder){
    
  }
  
  ngOnInit(): void {
    this.sendMoneyForm = this.Fb.group({
      receivingBank: ['', Validators.required],
      beneficiaryBank: ['', Validators.required],
      amount: ['', [Validators.required, Validators.pattern(/^\d+$/), Validators.min(1)]]
    })
  }

  onSubmit() {
    if (this.sendMoneyForm.valid){

      console.log('Form is Valid!');
    }
    else{
      console.log('Form is invalid');
    }
  }

}
