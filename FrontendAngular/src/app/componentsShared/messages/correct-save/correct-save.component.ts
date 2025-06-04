import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-correct-save',
  imports: [],
  templateUrl: './correct-save.component.html',
  styleUrl: './correct-save.component.css'
})
export class CorrectSaveComponent implements OnInit{
  @Input() message: string = 'Changes saved successfully!';
  messageCorrect: string = '';
  ngOnInit(): void {
      this.messageCorrect = this.message;  
  }
}
