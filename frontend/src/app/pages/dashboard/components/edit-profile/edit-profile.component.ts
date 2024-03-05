import { Component, OnInit } from '@angular/core';
import { User } from '../../../../models/responses';
import { UserService } from '../../../../services/user/user.service';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [],
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.css'
})
export class EditProfileComponent implements OnInit {
  user: User | undefined

  constructor(
    private userService: UserService
  ) {}

  async ngOnInit(): Promise<void> {
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
