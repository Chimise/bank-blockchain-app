// import { Component, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';

// interface Transaction {
//   id: string;
//   accountName: string;
//   date: string;
//   type: string;
//   amount: number; // Positive for credit, negative for debit
// }

// @Component({
//   selector: 'app-transaction-history',
//   standalone: true,
//   imports: [CommonModule],
//   templateUrl: './transaction-history.component.html',
//   styleUrl: './transaction-history.component.css'
// })
// export class TransactionHistoryComponent implements OnInit{
//   transactions: Transaction[] = [
//     { id: 'TX123', accountName: 'Savings Account', date: '2024-02-28', type: 'Satellite Bills', amount: -150 },
//     { id: 'TX124', accountName: 'Checking Account', date: '2024-02-27', type: 'Shopping Service', amount: 200 },
//     { id: 'TX125', accountName: 'Savings Account', date: '2024-02-26', type: 'Grocery', amount: -50 }
//   ];

//   i = 1;
//   constructor() { }

//   ngOnInit(): void {
//   } tr

// }

import { CommonModule } from '@angular/common';
import { Component, ElementRef, HostListener, OnInit } from '@angular/core';

@Component({
  selector: 'app-transaction-history',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './transaction-history.component.html',
  styleUrls: ['./transaction-history.component.css']
})
export class TransactionHistoryComponent implements OnInit {
  transactions = [
    { accountType: 'Savings', transactionDate: '2024-03-01', transactionId: 'TX001', narration: 'Deposit', amount: 5000 },
    { accountType: 'Checking', transactionDate: '2024-03-02', transactionId: 'TX002', narration: 'Withdrawal', amount: -2000 },
    { accountType: 'Savings', transactionDate: '2024-03-03', transactionId: 'TX003', narration: 'Transfer', amount: -1000 },
    { accountType: 'Investment', transactionDate: '2024-03-04', transactionId: 'TX004', narration: 'Deposit', amount: 10000 },
    { accountType: 'Savings', transactionDate: '2024-03-05', transactionId: 'TX005', narration: 'Withdrawal', amount: -3000 },
    { accountType: 'Checking', transactionDate: '2024-03-06', transactionId: 'TX006', narration: 'Deposit', amount: 4000 },
    { accountType: 'Investment', transactionDate: '2024-03-07', transactionId: 'TX007', narration: 'Transfer', amount: -500 },
    { accountType: 'Savings', transactionDate: '2024-03-08', transactionId: 'TX008', narration: 'Withdrawal', amount: -2000 }
  ];

  searchText: string = '';
  selectedTransaction: any;

  constructor(private elementRef: ElementRef) { }

  ngOnInit(): void {
    this.sortTransactions();
  }

  get filteredTransactions() {
    return this.transactions.filter(transaction =>
      transaction.accountType.toLowerCase().includes(this.searchText.toLowerCase()) ||
      transaction.transactionId.includes(this.searchText)
    );
  }

  sortTransactions() {
    // Sort transactions array based on transactionDate in descending order
    this.transactions.sort((a, b) => {
      return new Date(b.transactionDate).getTime() - new Date(a.transactionDate).getTime();
    });
  }


  viewTransaction(transaction: any) {
    this.selectedTransaction = transaction;
  }

  closeModal() {
    this.selectedTransaction = null;
  }

  @HostListener('document:click', ['$event'])
  onClick(event: any) {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      // Close the modal if the click event occurred outside the modal content
      this.closeModal();
    }
  }

  onSearchChange(searchText: string) {
    this.searchText = searchText;
  }
}

