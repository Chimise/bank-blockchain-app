import { Component } from '@angular/core';
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { HeaderComponent } from './components/dash-header/dash-header.component';
import { MainComponent } from './components/main/main.component';
import { ContainerComponent } from '../../components/container/container.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [SideBarComponent, HeaderComponent, MainComponent, ContainerComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {

}
