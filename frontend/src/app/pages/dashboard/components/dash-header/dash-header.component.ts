import { Component, OnInit } from '@angular/core';
import { ContainerComponent } from '../../../../components/container/container.component';
import { NavlinkComponent } from '../../../../components/navlink/navlink.component';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../../services/auth/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [ContainerComponent, NavlinkComponent, CommonModule, RouterLink],
  templateUrl: './dash-header.component.html',
  styleUrl: './dash-header.component.css'
})
export class HeaderComponent implements OnInit {
  showDropdown: boolean = false;
  showNotifications: boolean = false;

  userMenuItems = [
    { name: 'Profile', route: '/profile' },
    { name: 'Settings', route: '/settings' },
    { name: 'Logout', route: '/logout' }
  ];

  constructor(private authservice: AuthService) { }

  ngOnInit(): void {
  }

  logout(): void {
    this.authservice.logout();
  }

  toggleDropdown(): void {
    this.showDropdown = !this.showDropdown;
  }

  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
  }
}

