import { Component, OnInit } from '@angular/core';
import { ContainerComponent } from '../../../../components/container/container.component';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MenuItem } from '../../model/menu';

@Component({
  selector: 'app-side-bar',
  standalone: true,
  imports: [ContainerComponent, RouterLink, CommonModule],
  templateUrl: './side-bar.component.html',
  styleUrl: './side-bar.component.css'
})
export class SideBarComponent implements OnInit{
  isOpen: boolean = true;
  menus: MenuItem[] = [
    { name: 'Account', route: './account', icon: 'credit-card'},
    { name: 'History', route: './history', icon: 'payment'},
    { name: 'Profile', route: './profile', icon: 'user'},
    { name: 'Logout', route: './logout', icon: 'logout'}
  ];

  
  constructor() {}

  ngOnInit(): void {
    
  }

  toggleSidebar(): void {
    this.isOpen = !this.isOpen;
  }
}
