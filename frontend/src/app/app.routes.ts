import { Routes } from '@angular/router';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { NotfoundComponent } from './pages/notfound/notfound.component';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { EditProfileComponent } from './pages/edit-profile/edit-profile.component';
import { AccountComponent } from './pages/dashboard/components/account/account.component';




export const routes: Routes = [
    {
        component: DashboardComponent,
        path: "dashboard"
    },
    {
        component: LoginComponent,
        path: "login"
    },
    {
        component: HomepageComponent,
        path: ""
    },
    {
        component: DashboardComponent,
        path: "dashboard",
        children: [
            { path: '', redirectTo: 'account', pathMatch: 'full' },
            { path: 'account', component: AccountComponent },
          ],
    },
    {
        component: EditProfileComponent,
        path: "profile"
    },
    
    {
        component: NotfoundComponent,
        path: "**"
    }
];
