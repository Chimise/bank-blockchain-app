import { CommonModule, NgStyle } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-account-info-card',
  standalone: true,
  imports: [NgStyle],
  templateUrl: './account-info-card.component.html',
  styleUrl: './account-info-card.component.css'
})
export class AccountInfoCardComponent {
  @Input() title: string = '';
  @Input() description: string = '';
  @Input() src: string = '';
  @Input() svgBgColor: string = '';
  @Input() hoverBorderColor: string = '';
  @Input() hoverShadowColor: string = '';
  @Input() hover = false;
}
