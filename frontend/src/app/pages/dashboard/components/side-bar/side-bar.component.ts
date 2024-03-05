import { Component, OnInit } from '@angular/core';
import { ContainerComponent } from '../../../../components/container/container.component';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MenuItem } from '../../model/menu';
import { AuthService } from '../../../../services/auth/auth.service';

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
    { name: 'Account', route: '/dashboard/account', icon: 'credit-card'},
    { name: 'History', route: '/dashboard/history', icon: 'payment'},
    { name: 'Profile', route: '/dashboard/profile', icon: 'user'},
    { name: 'Logout', route: '/dashboard/logout', icon: 'logout'}
  ];

  
  constructor(private authService: AuthService) {}

  ngOnInit(): void {}

  logout(): void {
    this.authService.logout();
  }

  toggleSidebar(): void {
    this.isOpen = !this.isOpen;
  }
}
