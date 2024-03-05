import { Component, OnInit } from '@angular/core';
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { HeaderComponent } from './components/dash-header/dash-header.component';
import { RouterOutlet } from '@angular/router';
import { AccountComponent } from './components/account/account.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [SideBarComponent,HeaderComponent,RouterOutlet,AccountComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  constructor() { }

  ngOnInit(): void {
  }

}

