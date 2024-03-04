import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-sendmoney-popup',
  standalone: true,
  imports: [FormsModule,
  ReactiveFormsModule
],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class SendMoneyPopupComponent implements OnInit{

  sendMoneyForm:FormGroup = new FormGroup({});

  @Output() send = new EventEmitter<void>();
  @Output() closePopup = new EventEmitter<void>();

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
      this.send.emit();
    }
    else{
      console.log('Form is invalid');
    }
  }

  closeButtonClicked() {
    this.closePopup.emit();
  }
  


}
