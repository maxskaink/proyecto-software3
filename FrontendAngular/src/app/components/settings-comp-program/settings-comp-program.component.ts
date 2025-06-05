import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

// Services
import { ProgramCompetencyService } from '../../services/program-competency.service';

// Models
import { ProgramCompetency } from '../../models/ProgramCompetencyDTO';
import { TextBlock } from '../../componentsShared/templates/template-listbox-complete/template-listbox-complete.component';

// Components
import { TemplateBlockGridComponent } from '../../componentsShared/templates/template-block-grid/template-block-grid.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { ModalConfirmComponent } from "../../componentsShared/messages/modal-confirm/modal-confirm.component";
import { MatDialog } from '@angular/material/dialog';


@Component({
  selector: 'app-settings-comp-program',
  imports: [
    CommonModule,
    TemplateBlockGridComponent,
    TemplateHeaderTitleComponent,
],
  templateUrl: './settings-comp-program.component.html',
  styleUrl: './settings-comp-program.component.css'
})
export class SettingsCompProgramComponent implements OnInit {
  programCompetencies: ProgramCompetency[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private programCompetencyService: ProgramCompetencyService, 
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    console.log('SettingsCompProgramComponent initialized');
    this.loadInitialData();
  }

  private loadInitialData(): void {
    console.log('Loading initial data...');
    this.loadProgramCompetencies();
  }

  private loadProgramCompetencies(): void {
    console.log('Fetching program competencies...');
    this.loading = true;
    this.error = null;
    
    this.programCompetencyService.getAll().subscribe({
      next: (data) => {
        console.log('Program competencies received:', data);
        this.programCompetencies = data;
        
        // Check if we received data but the array is empty
        if (data && Array.isArray(data) && data.length === 0) {
          console.warn('Received empty array of program competencies');
        }
        
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading program competencies:', error);
        this.error = 'Unable to load program competencies';
        this.loading = false;
      },
      complete: () => {
        console.log('Program competencies request completed');
      }
    });
  }

  // Force reload competencies method for debugging
  reloadCompetencies(): void {
    console.log('Manually reloading competencies...');
    this.loadProgramCompetencies();
  }

  handleBlockClick(block: TextBlock): void {
    if (block.title.toLowerCase().includes('listar')) {
      const element = document.getElementById('createAsignature');
      if (element) {
        element.scrollIntoView({ behavior: 'smooth' });
      }
    } else if (block.route) {
      this.router.navigate([block.route]);
    }
  }

  onCreateCompetency(): void {
    console.log('Create competency event triggered');
    this.router.navigate(['settings/competencyProgram/create']);
  }

  onDeleteCompetency(index: number): void {
    console.log('Delete competency event triggered for index:', index);
    const competency = this.programCompetencies[index];
    
    if (competency && competency.id) {

      this.dialog.open(ModalConfirmComponent, {
        data: {
          message: '¿Está seguro que desea borrar la competencia?'
        }
      }).afterClosed().subscribe(result => {
        if(!result)
          return;
        this.loading = true;
        
        this.programCompetencyService.delete(competency.id).subscribe({
          next: () => {
            console.log('Competency deleted successfully');
            
            // Reload all competencies from the server to ensure we have the most up-to-date list
            this.loadProgramCompetencies();
          },
          error: (error) => {
            console.error('Error deleting competency:', error);
            this.error = 'Unable to delete competency';
            this.loading = false; // Reset loading state on error
          }
        });
      })

    } else {
      console.warn('Invalid competency or ID for deletion');
    }
  }
}