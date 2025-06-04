import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { RubricDTO } from '../../../models/RubricDTO';
import { CommonModule } from '@angular/common';
import { LevelDTO } from '../../../models/LevelDTO';
import { CriterionDTO } from '../../../models/CirterionDTO';
import { MoleculeLevelBoxComponent } from '../../molecules/molecule-level-box/molecule-level-box.component';
import { FormsModule } from '@angular/forms';
import { CriterionEntity } from '../../../models/CriterionEntity';
import { LevelEntity } from '../../../models/LevelEntity';
import { ModalConfirmComponent } from '../../messages/modal-confirm/modal-confirm.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-template-rubric-row',
  imports: [CommonModule, MoleculeLevelBoxComponent, FormsModule],
  templateUrl: './template-rubric-row.component.html',
  styleUrl: './template-rubric-row.component.css'
})
export class TemplateRubricRowComponent implements OnInit{
  @Input() criterion: CriterionEntity= {} as CriterionEntity;
  @Input() state: 'primary' |'secondary' = 'primary';
  @Output() onEdit = new EventEmitter<void>();
  @Output() onDelete = new EventEmitter<void>();
  levels: LevelEntity[] | null = {} as LevelEntity[]; 
  name: string = '';
  weight: number = 0; 
  constructor(private dialog: MatDialog) {} 

  handleDelete() {
    const dialogRef = this.dialog.open(ModalConfirmComponent, {
      data: {
        message: '¿Está seguro que desea eliminar este criterio?'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.onDelete.emit();
      }
    });
  }
  ngOnInit(): void {
    if (this.criterion && this.criterion.name) {
      this.name= this.criterion.name;
      this.levels= this.criterion.levels;
      this.weight = this.criterion.weight;
    }

  }

}
