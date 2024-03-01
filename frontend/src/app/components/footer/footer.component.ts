import { Component, NgModule } from '@angular/core';
import { ContainerComponent } from '../container/container.component';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [ContainerComponent, RouterLink, RouterLinkActive],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})


export class FooterComponent {
  currentYear = new Date().getFullYear();
}
