import { CommonModule } from '@angular/common';
import { Component, Input, OnInit} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CriterionService } from '../../../services/criterion.service';
import { CriterionDTO } from '../../../models/CirterionDTO';
import { MoleculeRubricLevelForCreateComponent } from '../../molecules/molecule-rubric-level-for-create/molecule-rubric-level-for-create.component';
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
    value: ''
  };
  touched = {
    name: false,
    value: false
  };
  @Input() idRubric: number = 0;
  
  
  constructor(private criterionService: CriterionService) {}

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

 /**
  * Save the criterion 
  * @returns void
  */
  saveCriterion(): void {
    if (!this.validateInputs()) {
      return;
    }

    const newCriterion: Partial<CriterionDTO> = {
      name: this.name,
      weight: this.weight,
      levels: []
    };

    this.criterionService.assignCriterionToRubric(this.idRubric, newCriterion)
      .subscribe({
        next: (response) => {
          console.log('Criterion saved successfully:', response);
          // Reset form
          this.name = '';
          this.weight = 0;
          this.errors = { name: '', value: '' };
        },
        error: (error) => {
          console.error('Error saving criterion:', error);
        }
      });
  }



  onInput(): void {
    this.validateInputs();
  }
}
