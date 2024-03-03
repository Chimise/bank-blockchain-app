import { CanActivateFn, Router } from '@angular/router';
import { Injectable, inject } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';

@Injectable({ providedIn: "root" })
class PermissionService {
  constructor(
    private authService: AuthService,
    private router: Router,
  ) { }

  async canActivate(): Promise<boolean> {
    if (await this.authService.isAuthenticated()) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}

export const authGuard: CanActivateFn = (route, state) => {
  // return true;
  return inject(PermissionService).canActivate();
};
