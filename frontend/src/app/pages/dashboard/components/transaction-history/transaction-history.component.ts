import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Transaction {
  id: string;
  accountName: string;
  date: string;
  type: string;
  amount: number; // Positive for credit, negative for debit
}

@Component({
  selector: 'app-transaction-history',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './transaction-history.component.html',
  styleUrl: './transaction-history.component.css'
})
export class TransactionHistoryComponent implements OnInit{
  transactions: Transaction[] = [
    { id: 'TX123', accountName: 'Savings Account', date: '2024-02-28', type: 'Satellite Bills', amount: -150 },
    { id: 'TX124', accountName: 'Checking Account', date: '2024-02-27', type: 'Shopping Service', amount: 200 },
    { id: 'TX125', accountName: 'Savings Account', date: '2024-02-26', type: 'Grocery', amount: -50 }
  ];

  i = 1;
  constructor() { }

  ngOnInit(): void {
  }

}
