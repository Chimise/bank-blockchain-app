import { Component, OnInit } from '@angular/core';
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { HeaderComponent } from './components/dash-header/dash-header.component';
import { MainComponent } from './components/main/main.component';
import { RouterOutlet } from '@angular/router';
import { ContainerComponent } from '../../components/container/container.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [SideBarComponent, HeaderComponent, RouterOutlet, MainComponent, ContainerComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  constructor() { }

  ngOnInit(): void {
  }

}

