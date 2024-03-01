import { Routes } from '@angular/router';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { NotfoundComponent } from './pages/notfound/notfound.component';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';


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
        component: NotfoundComponent,
        path: "**"
    }
];
