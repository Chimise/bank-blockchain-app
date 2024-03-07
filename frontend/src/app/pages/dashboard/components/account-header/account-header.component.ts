import { Component, OnInit } from '@angular/core';
import { User } from '../../../../models/responses';
import { UserService } from '../../../../services/user/user.service';

@Component({
  selector: 'app-account-header',
  standalone: true,
  imports: [],
  templateUrl: './account-header.component.html',
  styleUrl: './account-header.component.css'
})
export class AccountHeaderComponent {
  user: User | undefined

  constructor(private userService: UserService) {}

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
}
