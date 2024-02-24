import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { NotfoundComponent } from './pages/notfound/notfound.component';
import { LoginComponent } from './pages/login/login.component';
import { AuthGuard } from './services/auth.guard';


export const routes: Routes = [
    {
        component: LoginComponent,
        path: ""
    },
    {
        component: HomepageComponent,
        path: "home",
        canActivate: [AuthGuard]
    },
    {
        component: NotfoundComponent,
        path: "404"
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  export class AppRoutingModule { }