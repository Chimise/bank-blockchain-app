import { Component } from '@angular/core';
import { ContainerComponent } from '../container/container.component';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { NavlinkComponent } from '../navlink/navlink.component';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [ContainerComponent, RouterLink, RouterLinkActive, NavlinkComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

}
