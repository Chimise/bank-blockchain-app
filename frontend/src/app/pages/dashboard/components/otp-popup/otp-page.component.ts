import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../../../services/user/user.service';
import { User } from '../../../../models/responses';

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
  user: User | undefined

  @Output() closeNewModal = new EventEmitter<void>();

  constructor(private otpForm: FormBuilder, private userService: UserService){

  }

  async OnInit(): Promise<void> {
    try {
      const isAuthenticated = await this.userService.getUser();

      if (isAuthenticated) {
        this.user = await this.userService.getUser();
      } else {
        console.log('User is not authenticated');
        
      }
    } catch (error) {
      console.error('Error retrieving user:', error);
    }
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

keytab(event: any){
  let nextInput = event.srcElement.nextElementSibling; // get the sibling element

  var target = event.target || event.srcElement;
  var id = target.id


  if(nextInput == null)  // check the maxLength from here
      return;
  else
      nextInput.focus();   // focus if not null
}

closeButtonClicked() {
  this.closeNewModal.emit();
}

}
