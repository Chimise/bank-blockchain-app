import { Routes } from '@angular/router';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { NotfoundComponent } from './pages/notfound/notfound.component';
import { LoginComponent } from './pages/login/login.component';

import { AccountComponent } from './pages/account/account.component';
import { AccountConfirmationComponent } from './pages/account-confirmation/account-confirmation.component';
import { OtpPageComponent } from './pages/otp-page/otp-page.component';

import { EditProfileComponent } from './pages/edit-profile/edit-profile.component';



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

        component: AccountComponent,
        path: "account"
    },
    {
        component: AccountConfirmationComponent,
        path: "account/confirm"
    },
    {
        component: OtpPageComponent,
        path: "account/confirm/otp"
    },
    {
        component: EditProfileComponent,
        path: "update-profile"
    },
    
    {
        component: NotfoundComponent,
        path: "**"
    }
];
