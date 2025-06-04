import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output, Inject} from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
@Component({
  selector: 'app-modal-confirm',
  imports: [CommonModule],
  templateUrl: './modal-confirm.component.html',
  styleUrl: './modal-confirm.component.css'
})
export class ModalConfirmComponent {
    @Output () confirm = new EventEmitter<void>();
    @Output () cancel = new EventEmitter<void>();
    @Input() message: string = 'Are you sure you want to proceed?';
    constructor(
      public dialogRef: MatDialogRef<ModalConfirmComponent>,
      @Inject(MAT_DIALOG_DATA) public data: { message: string }
    ) {}
  
    onCancel(): void {
      this.dialogRef.close(false);
    }
  
    onConfirm(): void {
      this.dialogRef.close(true);
    }
}
