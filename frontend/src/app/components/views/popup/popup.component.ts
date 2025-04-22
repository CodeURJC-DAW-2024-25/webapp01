import { Component, EventEmitter, Input, Output } from '@angular/core';



@Component({
  selector: 'app-popup',
  templateUrl: './popup.component.html',
  styleUrl: './popup.component.css'
})
export class PopupComponent {

  @Input() header: string = '';
  @Input() content: string = '';

  @Output() popupClosed = new EventEmitter();

  closePopup(): void {
    this.popupClosed.emit();
  }

  ngOnInit(): void {
    
    setTimeout(() => this.closePopup(), 5000);
  }
}