import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-alertmessage',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './alertmessage.component.html',
  styleUrl: './alertmessage.component.css'
})
export class AlertmessageComponent implements OnInit {
  @Input() message: string = 'Changes saved successfully!';
  @Input() state: 'save' | 'error' | 'correct' = 'save';
  @Output() closed = new EventEmitter<void>();
  isHiding: boolean = false;

  ngOnInit() {
    setTimeout(() => {
      this.hideMessage();
    }, 2000);
  }

  hideMessage() {
    if (!this.isHiding) {
      //this.isHiding = true;
      setTimeout(() => {
        this.closed.emit();
      }, 5000);
    }
  }
}
