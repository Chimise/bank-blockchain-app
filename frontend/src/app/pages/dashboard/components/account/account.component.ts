import { Component } from '@angular/core';
import { AccountHeaderComponent } from '../account-header/account-header.component';
import { AccountInfoCardComponent } from '../account-info-card/account-info-card.component';
import { SendMoneyComponent } from '../send-money/send-money.component';
import { TransactionHistoryComponent } from '../transaction-history/transaction-history.component';
import { SendMoneyPopupComponent } from '../sendmoney-popup/account.component';
import { CommonModule, NgIf } from '@angular/common';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [AccountHeaderComponent, AccountInfoCardComponent, SendMoneyComponent, TransactionHistoryComponent, SendMoneyPopupComponent, NgIf, CommonModule],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {
  info = [
    {id: 1, title: "My Balance", description: "#50,000", src: "assets/money-tag 1.svg", svgBgColor: "rgba(255, 245, 217, 1)", hoverBorderColor: "rgba(244, 151, 76, 1)", hoverShadowColor: "rgba(255, 245, 217, 1)"},
    {id: 2, title: "Received", description: "#20,500", src: "assets/Group.svg", svgBgColor: "rgba(231, 237, 255, 1)", hoverBorderColor: "rgba(0, 169, 212, 1)", hoverShadowColor: "rgba(231, 237, 255, 1)"},
    {id: 3, title: "Expense", description: "#10,250", src: "assets/001-medical.svg", svgBgColor: "rgba(242, 218, 239, 1)", hoverBorderColor: "rgba(151, 83, 156, 1)", hoverShadowColor: "rgba(242, 218, 239, 1)"},
    {id: 4, title: "Savings", description: "#25,900", src: "assets/003-saving.svg", svgBgColor: "rgba(232, 243, 218, 1)", hoverBorderColor: "rgba(124, 187, 76, 1)", hoverShadowColor: "rgba(232, 243, 218, 1)"},
  ]

  showPopup = false;

  togglePopup() {
    this.showPopup = !this.showPopup;
  }
}
