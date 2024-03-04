import { Routes } from '@angular/router';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { NotfoundComponent } from './pages/notfound/notfound.component';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { EditProfileComponent } from './pages/dashboard/components/edit-profile/edit-profile.component';
import { AccountComponent } from './pages/dashboard/components/account/account.component';
import { authGuard } from './guard/auth/auth.guard';

export const routes: Routes = [
  {
    component: LoginComponent,
    path: 'login',
  },
  {
    component: DashboardComponent,
    path: 'dashboard',
    children: [
      { path: 'account', component: AccountComponent },
      { path: '', redirectTo: '/dashboard/account', pathMatch: 'full' },
    ],
    canActivate: [authGuard],
  },
  {
    component: EditProfileComponent,
    path: 'profile',
    canActivate: [authGuard],
  },
  {
    component: HomepageComponent,
    path: '',
  },
  {
    component: NotfoundComponent,
    path: '**',
  },
];
