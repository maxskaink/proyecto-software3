import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CriterionService } from '../../../services/criterion.service';
import { LevelService } from '../../../services/level.service';

import { CriterionDTO } from '../../../models/CirterionDTO';
import { MoleculeRubricLevelForCreateComponent } from '../../molecules/molecule-rubric-level-for-create/molecule-rubric-level-for-create.component';
import { LevelDTO } from '../../../models/LevelDTO';
import { LevelEntity } from '../../../models/LevelEntity';
import { CriterionEntity} from '../../../models/CriterionEntity';
@Component({
  selector: 'app-template-rubric-create-criterion',
  imports: [CommonModule, FormsModule, MoleculeRubricLevelForCreateComponent],
  templateUrl: './template-rubric-create-criterion.component.html',
  styleUrl: './template-rubric-create-criterion.component.css'
})
export class TemplateRubricCreateCriterionComponent  {
  
  weight:number =  0;
  name:string = ''; 
  errors = {
    name: '',
    value: '',
    levelError:  ''
  };
  touched = {
    name: false,
    value: false
  };
  MAX_LEVEL: number = 5;
  Levels: LevelEntity[] = [
    {  description: 'Nivel Básico', percentage: 33 },
    {  description: 'Nivel Intermedio', percentage: 33 },
    {  description: 'Nivel Avanzado', percentage: 34 }
  ];
  @Input() idRubric: number = 38;
  @Output() criterionAdded = new EventEmitter<CriterionEntity>();

  
  
  constructor(private criterionService: CriterionService,
    private levelService: LevelService,
  ) {}

  /**
   * Verify the input of create criterion
   * @returns true or flase, true is all correct, false is incorrect input
   */
  validateInputs(): boolean {
    let isValid = true;
    
    if (this.name.length > 100) {
      this.errors.name = 'El nombre no puede exceder los 100 caracteres';
      isValid = false;
    } else if (!this.name.trim()) {
      this.errors.name = 'El nombre es requerido';
      isValid = false;
    } else {
      this.errors.name = '';
    }

    if (this.weight < 0 || this.weight > 100) {
      this.errors.value = 'El valor debe estar entre 0 y 100';
      isValid = false;
    } else {
      this.errors.value = '';
    }

    return isValid;
  }


  createNewLevel(): LevelEntity {
    const newLevel: LevelEntity  = {
      description: '',
      percentage: 0,
    }
    return newLevel;
  }

  addNewLevel(): void {
    if (this.Levels.length >= this.MAX_LEVEL) {
      console.error('Maximum number of levels reached');
      this.errors.levelError = 'No se pueden agregar más niveles';
      return;
    }
    const newLevel: LevelEntity = this.createNewLevel();
    this.Levels.push(newLevel);
  }
  

  onInput(): void {
    this.validateInputs();
  }

  onLevelChange(event: {index: number, level: LevelEntity}): void {
    this.Levels[event.index] = event.level;
  }
  /**
   * Method to save levels associated with the criterion
   * This method iterates through the Levels array and sends each level to the backend service
   * @returns void
   */
  saveLevels(): void {
    this.Levels.forEach((level) => {
      this.levelService.assignLevelToCriterion(this.idRubric, level)
        .subscribe({
          next: (response) => {
            console.log('Level saved successfully:', response);
          },
          error: (error) => {
            console.error('Error saving level:', error);
            this.errors.levelError = 'Error al guardar los niveles';
          }
        });
    });
  }
  handleRedIndicator(levelIndex: number): void {
    // Remove the clicked level
    this.Levels.splice(levelIndex, 1);
  }
  createNewCriterion(): CriterionEntity {
    const newCriteron: CriterionEntity= {
      name: this.name,
      weight: this.weight,
      levels: this.Levels 
    };
    return newCriteron;
  }
  addCriterionWithLevels(): void {
    if (!this.validateInputs()) {
      return;
    } 
    const newCriterion:CriterionEntity = this.createNewCriterion();
    this.criterionAdded.emit(newCriterion);

    // Reset form
    this.name = '';
    this.weight = 0;
    this.Levels = [
      { description: 'Nivel Básico', percentage: 33 },
      { description: 'Nivel Intermedio', percentage: 33 },
      { description: 'Nivel Avanzado', percentage: 34 }
    ];
    this.errors = { name: '', value: '', levelError: '' };
  }
  deleteLevel(index: number): void {
    if (this.Levels.length > 1) {
      this.Levels.splice(index, 1);
    } else {
      this.errors.levelError = 'Debe haber al menos un nivel';
    }
  }
}
