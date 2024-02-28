import { Component } from '@angular/core';
import { AccountHeaderComponent } from '../account-header/account-header.component';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [AccountHeaderComponent],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {

}
