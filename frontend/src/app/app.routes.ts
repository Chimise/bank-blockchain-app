import { Routes } from '@angular/router';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { NotfoundComponent } from './pages/notfound/notfound.component';
import { LoginComponent } from './pages/login/login.component';


export const routes: Routes = [
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
