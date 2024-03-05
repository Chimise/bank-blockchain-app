import { Routes } from '@angular/router';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { NotfoundComponent } from './pages/notfound/notfound.component';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { EditProfileComponent } from './pages/edit-profile/edit-profile.component';
import { AccountComponent } from './pages/dashboard/components/account/account.component';
import { authGuard } from './guard/auth/auth.guard';
import { TransactionHistoryComponent } from './pages/dashboard/components/transaction-history/transaction-history.component';

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
      {path: 'history', component: TransactionHistoryComponent},
      {path: 'profile', component: EditProfileComponent },
      { path: '', redirectTo: '/dashboard/account', pathMatch: 'full' },
    ],
    canActivate: [authGuard],
  },
  // {
  //   component: EditProfileComponent,
  //   path: 'profile',
  //   // canActivate: [authGuard],
  // },
  {
    component: HomepageComponent,
    path: '',
  },
  {
    component: NotfoundComponent,
    path: '**',
  },
];
