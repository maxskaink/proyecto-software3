import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TemplateInputBoxtextComponent } from '../../componentsShared/templates/template-input-boxtext/template-input-boxtext.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { MoleculeOutComeComponent } from '../../componentsShared/molecules/molecule-out-come/molecule-out-come.component';
import { CommonModule } from '@angular/common';
import { ProgramOutcomeService } from '../../services/program-outcome.service';
import { SubjectOutomeService } from '../../services/subject_outcome.service';
import { SubjectOutcome } from '../../models/SubjectOutcomeDTO';
import { ProgramOutcome } from '../../models/ProgramOutcomeDTO';

//This component handles both general competency data and subject-specific competency data 
// based on the presence of a subjectId in the query parameters.

@Component({
  selector: 'app-competency',
  imports: [CommonModule, TemplateInputBoxtextComponent, 
    TemplateHeaderTitleComponent, MoleculeOutComeComponent],
  templateUrl: './competency.component.html',
  styleUrl: './competency.component.css'
})
export class CompetencyComponent implements OnInit {
  subjectId?: number;
  isSubjectSpecific = false;

  subjectOutcomes: SubjectOutcome[] = [];
  programOutcomes: ProgramOutcome[] = [];
  loading: boolean = false;
  error: string | null = null;
  maxOutcomes: number = 1; 

  constructor(
    private route: ActivatedRoute,
    private programOutcomeService: ProgramOutcomeService,
    private subjectOutcomeService: SubjectOutomeService
  ) { }

  ngOnInit(): void {
    // Get the subjectId from query parameters
    this.route.queryParams.subscribe(params => {
      if (params['subjectId']) {
        this.subjectId = +params['subjectId'];
        this.isSubjectSpecific = true;
        this.loadSubjectSpecificData();
      } else {
        this.loadGeneralData();
      }
    });
  }

  loadSubjectSpecificData(): void {
    if (!this.isSubjectSpecific || !this.subjectId) {
      return;
    }
    
    this.loading = true;
    this.error = null;
    
    this.subjectOutcomeService.getOutcomesBySubject(this.subjectId).subscribe({
      next: (data) => {
        this.subjectOutcomes = data;
        this.loading = false;
        console.log('Subject-specific outcomes loaded:', data);
      },
      error: (err) => {
        this.error = 'Failed to load outcomes. Please try again.';
        this.loading = false;
        console.error('Error loading subject-specific outcomes:', err);
      }
    });
  }

  loadGeneralData(): void {
    this.loading = true;
    this.error = null;

    this.programOutcomeService.getAll().subscribe({
      next: (data) => {
        this.programOutcomes = data;
        this.loading = false;
        console.log('General outcomes loaded:', data);
      },
      error: (err) => {
        this.error = 'Failed to load outcomes. Please try again.';
        this.loading = false;
        console.error('Error loading general outcomes:', err);
      }
    });
  }

  hasMaxCompetencies(): boolean {
    if (this.isSubjectSpecific) {
      return this.subjectOutcomes.length >= this.maxOutcomes;
    } else {
      return this.programOutcomes.length >= this.maxOutcomes;
    }
  }

  getTitle(): string {
    return this.isSubjectSpecific ? 'Competencia de asignatura' : 'Competencia de programa';
  }
}