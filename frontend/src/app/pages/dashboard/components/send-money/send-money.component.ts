import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-send-money',
  standalone: true,
  imports: [],
  templateUrl: './send-money.component.html',
  styleUrl: './send-money.component.css'
})
export class SendMoneyComponent {
  @Output() buttonClick = new EventEmitter<void>();

  onButtonClick() {
    this.buttonClick.emit();
  }
}
