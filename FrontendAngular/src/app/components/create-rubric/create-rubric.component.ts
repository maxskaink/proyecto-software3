import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { TemplateRubricCreateCriterionComponent } from '../../componentsShared/templates/template-rubric-create-criterion/template-rubric-create-criterion.component';
import { ActivatedRoute } from '@angular/router';
import { MoleculeRubricLevelForCreateComponent } from '../../componentsShared/molecules/molecule-rubric-level-for-create/molecule-rubric-level-for-create.component';
import { CriterionDTO } from '../../models/CirterionDTO';
import { CriterionEntity } from '../../models/CriterionEntity';
import { TemplateRubricRowComponent } from '../../componentsShared/templates/template-rubric-row/template-rubric-row.component';
import { FormsModule } from '@angular/forms';
import { RubricDTO } from '../../models/RubricDTO';
import { LevelService } from '../../services/level.service';
import { RubricService } from '../../services/rubric.service';
import { CriterionService } from '../../services/criterion.service';
import { LevelDTO } from '../../models/LevelDTO';
import { firstValueFrom } from 'rxjs';
import { AlertmessageComponent } from '../../componentsShared/messages/alertmessage/alertmessage.component';
import { Router } from '@angular/router';
@Component({
  selector: 'app-create-rubric',
  imports: [CommonModule,
    TemplateHeaderTitleComponent,
    FormsModule,
    TemplateRubricCreateCriterionComponent,
    TemplateRubricRowComponent,
    AlertmessageComponent
  ],
  templateUrl: './create-rubric.component.html',
  styleUrl: './create-rubric.component.css'
})
export class CreateRubricComponent {
  idOutcome: number = -1;
  @Input() RubricDTO: RubricDTO= { } as RubricDTO
  idRubric: number = 38;
  criterioIsEdit: boolean = false;
  description: string = 'Escribe aqui la descripción de tu rubrica';
  idCriterion: number = -1;
  confirmBack:boolean =false;
  readonly MAX_CRITERIA: number = 5; // Add constant for max criteria

  editingCriterion: CriterionEntity | null = null; // Add this to track which criterion is being edited

  constructor(private route: ActivatedRoute,
    private rubricService: RubricService,
    private criterionService: CriterionService,
    private router: Router, // Add Router
    private levelSerice: LevelService) {}
  listCriterion: CriterionEntity[] = [];
  listCriterionExist: CriterionDTO[] = [];
  isEdit:boolean = false;
  showAlert: boolean = false;
  error= {
    weight: '',
    maxCriteria:'',
    description: ''
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      if (params['outcomeId']) {
        this.idOutcome = +params['outcomeId']; // Convertir a número
      } else {
        console.error('No outcome ID provided in URL parameters');
      }
          // Get rubricId if exists
    if (params['idRubric'] && params['idRubric'] !== '-1') {
      this.rubricService.getRubricById(+params['idRubric'])
        .subscribe({
          next: (rubric) => {
            this.RubricDTO = rubric;
            this.idRubric = rubric.id;
            this.description = rubric.description;
            this.listCriterion = rubric.criteria || [];
            this.isEdit = true;
          },
          error: (error) => {
            console.error('Error fetching rubric:', error);
          }
        });
      }
    });
    

  }
  /**
   * Saves the rubric by creating it, saving its criteria, and levels.
   * If any step fails, it will delete the rubric to maintain consistency.
   */
  async saveRubric(): Promise<void> {
    try {

      if (!this.validateCriterions()) {
        return;
      }
      if(this.isEdit){
        await this.editRubric();
        this.showAlert = true;
      }

      else{
        await this.createRubric();
        await this.saveCriterions();
        await this.saveLevels();
        this.showAlert = true; // Show alert on success
        console.log('Rubric created successfully with ID:', this.idRubric);
      }

      setTimeout(() => {
        this.router.navigate(['home/subject/competency/subject/outcome'], {
          queryParams: {
            outcomeId: this.idOutcome
          }
        });
      }, 1000);
      
    } catch (error) {
      console.error('Error in rubric creation process:', error);
      if (this.idRubric) {
        await this.deleteRubric();
      }
    }

  }

  async editRubric(): Promise<void>{
         // 1. Get existing criteria from DB
         this.isEdit = true;
         const existingCriteria = await firstValueFrom(
          this.criterionService.getAllCriterionByRubric(this.idRubric)
        );

        // 2. Delete all existing criteria
        for (const criterion of existingCriteria) {
          await firstValueFrom(
            this.criterionService.deleteCriterion(criterion.id)
          );
        }

        // 3. Create new criteria and their levels
        await this.saveCriterions();
        await this.saveLevels();

        // 4. Update rubric description if changed
        if (this.description !== this.RubricDTO.description) {
          await firstValueFrom(
              this.rubricService.updateRubric(this.idRubric, {
                  description: this.description
              } as Partial<RubricDTO>)
          );
          this.isEdit = false; // Reset editing state
      }
      this.isEdit = false; // Reset editing state

  }

  onAlertClosed() {
    this.showAlert = false;
  }
  /**
   * create the rubric if the validates of rubric is true
   */
  private async createRubric(): Promise<void> {
    if (!this.validateCriterions()) {
      throw new Error('Validation failed');
    }

    const response = await firstValueFrom(
      this.rubricService.assignRubricToSubjectOutcome(this.idOutcome, {
        description: this.description
      })
    );

    this.idRubric = response.id;
  }
  /**
   * Save criterion in a new rubric
   */
private async saveCriterions(): Promise<void> {
    for (const criterion of this.listCriterion) {
      const response = await firstValueFrom(
        this.criterionService.assignCriterionToRubric(this.idRubric, {
          name: criterion.name,
          weight: criterion.weight
        })
      );
      this.listCriterionExist.push(response);
    }
  }

  private async saveLevels(): Promise<void> {
    const criteriaFromDB = await this.getCriteriaFromDB();
    await this.validateAndAssignLevels(criteriaFromDB);
  }

  private async getCriteriaFromDB(): Promise<CriterionDTO[]> {
    return firstValueFrom(
      this.criterionService.getAllCriterionByRubric(this.idRubric)
    );
  }

  private async validateAndAssignLevels(criteriaFromDB: CriterionDTO[]): Promise<void> {
    if (!this.validateCriteriaCoherence(criteriaFromDB)) {
      throw new Error('Criteria coherence validation failed');
    }

    for (let i = 0; i < this.listCriterion.length; i++) {
      await this.assignLevelsForCriterion(
        this.listCriterionExist[i].id,
        this.listCriterion[i].levels
      );
    }
  }

  private validateCriteriaCoherence(criteriaFromDB: CriterionDTO[]): boolean {
    return this.listCriterion &&
           this.listCriterion.length > 0 &&
           this.listCriterionExist.length === this.listCriterion.length;
  }

  private async assignLevelsForCriterion(criterionId: number, levels: Partial<LevelDTO>[]): Promise<void> {
    for (const level of levels) {
      await firstValueFrom(
        this.levelSerice.assignLevelToCriterion(criterionId, {
          category: "low",
          description: level.description,
          percentage: level.percentage
        })
      );
    }
  }

  private async deleteRubric(): Promise<void> {
    await firstValueFrom(this.rubricService.deleteRubric(this.idRubric));
    console.log('Rubric deleted due to error');
  }
  validateCriterions():boolean{
    let totalPercentage: number = 0;
    for(const criterion of this.listCriterion){
       totalPercentage += criterion.weight;
    }
    if(totalPercentage >100 || totalPercentage <100){
      this.error.weight = 'Los criterios deben sumar en total 100%'
      this.clearErrorAfterDelay('weight');
      return false;
    }
    if(!this.description.trim()){
      this.error.description = 'La descripcion es obligatoria, no puede quedar vacia'
      this.clearErrorAfterDelay('description');
      return false; 
    }

    return true;
  }

  onCriterionAdded(criterion: CriterionEntity): void {
    if (this.editingCriterion) {
      const index = this.listCriterion.findIndex(c => c === this.editingCriterion);
      if (index !== -1) {
        this.listCriterion[index] = criterion;
      }
      this.editingCriterion = null;
      this.criterioIsEdit = false;
      this.confirmBack =false;
    } else {
      if (this.listCriterion.length >= this.MAX_CRITERIA) {
        this.error.maxCriteria = 'No se pueden agregar más de 5 criterios';
        return;
      }
      this.listCriterion.push(criterion);
    }
  }
  handleEditCriterion(criterion: CriterionEntity): void {
    this.criterioIsEdit = true; // Set the flag to indicate editing mode
    this.editingCriterion = criterion; // Store the criterion being edited
    this.confirmBack = true;
  }
  handleDeleteCriterion(criterion: CriterionEntity): void {
    // Lógica para eliminar el criterio
    const index = this.listCriterion.indexOf(criterion);
    if (index > -1) {
      this.listCriterion.splice(index, 1);
    }
    console.log('Criterio eliminado:', criterion);
  }
  private calculateTotalWeight(): number {
    return this.listCriterion.reduce((sum, criterion) => sum + criterion.weight, 0);
  }
  get shouldHideCreateCriterion(): boolean {
    return this.listCriterion.length >= this.MAX_CRITERIA || 
           this.calculateTotalWeight() >= 100;
  }
  private clearErrorAfterDelay(errorKey: keyof typeof this.error) {
    setTimeout(() => {
      this.error[errorKey] = '';
    }, 2000);
  }
  getTotalWeight(): number {
    return this.listCriterion?.reduce((sum, criterion) => sum + criterion.weight, 0) || 0;
  }
}
