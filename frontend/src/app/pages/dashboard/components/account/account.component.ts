import { Component } from '@angular/core';
import { AccountHeaderComponent } from '../account-header/account-header.component';
import { AccountInfoCardComponent } from '../account-info-card/account-info-card.component';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [AccountHeaderComponent, AccountInfoCardComponent],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {
  info = [
    {id: 1, title: "My Balance", description: "#50,000", src: "assets/money-tag 1.svg"},
    {id: 2, title: "Received", description: "#20,500", src: "assets/Group.svg"},
    {id: 3, title: "Expense", description: "#10,250", src: "assets/001-medical.svg"},
    {id: 4, title: "Total Saving", description: "#25,900", src: "assets/003-saving.svg"},
  ]
}
