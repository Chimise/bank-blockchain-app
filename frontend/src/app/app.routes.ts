import { Routes } from '@angular/router';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { NotfoundComponent } from './pages/notfound/notfound.component';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { EditProfileComponent } from './pages/dashboard/components/edit-profile/edit-profile.component';
import { AccountComponent } from './pages/dashboard/components/account/account.component';
import { authGuard } from './guard/auth/auth.guard';
import { AboutComponent } from './pages/about/about.component';
import { ServiceComponent } from './pages/service/service.component';
import { LegalComponent } from './pages/legal/legal.component';
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

  {
    path: 'settings', 
  redirectTo: '/dashboard/profile',
 },
  // {
  //   component: EditProfileComponent,
  //   path: 'profile',
  //   // canActivate: [authGuard],
  // },
  {
    component: AboutComponent,
    path: 'about'
  },
  {
    component: ServiceComponent,
    path: 'services'
  },
  {
    component: LegalComponent,
    path: 'legal'
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
