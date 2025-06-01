import { SubjectOutcome } from './../../../../models/SubjectOutcomeDTO';
import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialogModule,
} from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { catchError, Observable, of, tap, map } from 'rxjs';

@Component({
  selector: 'app-template-modal-reuse-outcome',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatIconModule, FormsModule],
  templateUrl: './template-modal-create-outcome.component.html',
  styleUrl: './template-modal-create-outcome.component.css',
})
export class TemplateModalCreateOutcomeComponent implements OnInit {
  loading = {
    outcomes: false,
  };

  error = {
    outcomes: false,
    duplicate: false,
  };

  inputValue: string = '';
  touched: boolean = false;

  constructor(
    public dialogRef: MatDialogRef<TemplateModalCreateOutcomeComponent>,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      newSubjectOutcome: SubjectOutcome;
      selectedOutcomes: SubjectOutcome[];
      textDescription?: string;
    }
  ) {}

  ngOnInit() {
    // Inicialización si es necesaria
  }

  saveOutcome() {
    this.touched = true;

    // Validar que el campo no esté vacío
    if (!this.inputValue.trim()) {
      return;
    }

    // Verificar si ya existe un outcome con la misma descripción
    const isDuplicate = this.data.selectedOutcomes.some(
      (outcome) =>
        outcome.description.trim().toLowerCase() ===
        this.inputValue.trim().toLowerCase()
    );

    if (isDuplicate) {
      this.error.duplicate = true;
      return;
    }

    // Crear nuevo outcome y cerrar el diálogo
    const newOutcome: SubjectOutcome = {
      id: Math.floor(Math.random() * -1000), // ID temporal negativo para nuevos
      description: this.inputValue.trim(),
      rubric: null,
    };

    this.dialogRef.close(newOutcome);
  }

  onBlur() {
    this.touched = true;
  }

  getDescription(): string {
    return this.data.textDescription || '';
  }

  // Añadir este método a la clase TemplateModalCreateOutcomeComponent
  clearErrors() {
    // Si el usuario cambia el texto, eliminar los mensajes de error
    this.error.duplicate = false;

    // Nota: No limpiamos el estado 'touched' para que la validación
    // de campo vacío siga funcionando hasta que haya contenido
  }
}
