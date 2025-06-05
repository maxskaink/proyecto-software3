import { CommonModule, NgClass } from '@angular/common';
import {
  Component,
  ElementRef,
  OnInit,
  ViewChild,
  AfterViewInit,
} from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import { MoleculeSearchBarComponent } from '../../componentsShared/molecules/molecule-search-bar/molecule-search-bar.component';
import { MoleculeBlockComponent } from '../../componentsShared/molecules/molecule-block/molecule-block.component';
import { SubjectDTO } from '../../models/SubjectDTO';
import { SubjectService } from '../../services/subject.service';
import { LoadingComponent } from '../../componentsShared/loading/loading.component';
import { MoleculeAsideMenuComponent } from '../../componentsShared/molecules/molecule-aside-menu/molecule-aside-menu.component';
import type { MenuItem } from '../../componentsShared/molecules/molecule-aside-menu/molecule-aside-menu.component';
import { SubjectOutomeService } from '../../services/subject_outcome.service';
import { AuthService } from '../../services/auth.service';
import { EvaluatorAssignmentService } from '../../services/evaluator_assignment.service';
import { Observable, forkJoin } from 'rxjs';

export interface listItem {
  id?: number;  // Add this line - optional ID for the item
  title: string;
  description: string;
}


@Component({
  selector: 'app-home',
  imports: [
    HeaderComponent,
    MoleculeBlockComponent,
    MoleculeSearchBarComponent,
    CommonModule,
    FormsModule,
    LoadingComponent,
    MoleculeAsideMenuComponent,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit, AfterViewInit {
  // Property declarations grouped by type
  // View state properties
  activeView: 'subjects' | 'outcomes' = 'subjects';
  isLoading: boolean = true;
  isDesktop: boolean = true;
  canScrollLeft = false;
  canScrollRight = true;

  // Data properties
  asignatures: SubjectDTO[] = [];
  asignaturesFilters: SubjectDTO[] = [];
  outcomes: any[] = []; // Store outcome data
  listItems: listItem[] = [];
  wordSearch: string = '';
  userID: string = '';

  // Menu configuration
  menuItems: MenuItem[] = [
    {
      title: 'Tus Asignaturas',
      active: true,
      action: () => {
        this.activeView = 'subjects';
        this.loadAsignatures();
      },
    },
    {
      title: 'RA a evaluar',
      active: false,
      action: () => {
        this.activeView = 'outcomes';
        this.loadOutcomes();
      },
    },
  ];

  @ViewChild('carousel', { static: false }) carousel!: ElementRef;

  constructor(
    private asignatureService: SubjectService,
    private router: Router,
    private route: ActivatedRoute,
    private subjectOutcomeService: SubjectOutomeService,
    private evaluatorAssignmentService: EvaluatorAssignmentService,
    private authService: AuthService
  ) {
    this.checkScreenSize();
    window.addEventListener('resize', () => this.checkScreenSize());
  }


  private truncateText(text: string, maxLength: number = 25): string {
    if (!text) return '';
    return text.length > maxLength
      ? text.substring(0, maxLength) + '...'
      : text;
  }

  // Lifecycle methods
  ngOnInit(): void {
    this.isLoading = true;

    // Get user ID and then load appropriate data
    this.authService.uid.subscribe({
      next: (uid) => {
        if (uid) {
          this.userID = uid;
          console.log('User ID loaded:', uid);

          // Load initial view data based on active view
          if (this.activeView === 'subjects') {
            this.loadAsignatures();
          } else {
            this.loadOutcomes();
          }
        } else {
          console.error('User ID not available');
          this.isLoading = false;

          // Default to loading subjects if no user ID
          this.loadAsignatures();
        }
      },
      error: (error) => {
        console.error('Error loading user ID:', error);
        this.isLoading = false;

        // Default to loading subjects on error
        this.loadAsignatures();
      }
    });
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.updateScrollButtons();
    }, 200);
  }

  ngOnDestroy() {
    window.removeEventListener('resize', () => this.checkScreenSize());
  }

  // Data loading methods
  private loadAsignatures(): void {
    this.isLoading = true;
    this.asignatureService.getAssignedSubject().subscribe({
      next: (data) => {
        this.asignatures = data;
        this.asignaturesFilters = [...this.asignatures];

        // Convert to listItems
        this.listItems = this.asignatures.map(asignature => ({
          title: this.truncateText(asignature.name),
          description: asignature.description
        }));


        this.isLoading = false;

        setTimeout(() => {
          this.updateScrollButtons();
        }, 100);
      },
      error: (error) => {
        console.error('Error loading subjects:', error);
        this.isLoading = false;
      },
    });
  }

  private loadOutcomes(): void {
    this.isLoading = true;
    this.listItems = []; // Clear previous items

    // First, check if we have a valid user ID
    if (!this.userID) {
      console.error('Cannot load outcomes: User ID not available');
      this.isLoading = false;
      return;
    }

    console.log('Loading outcomes for user:', this.userID);

    // Get evaluator assignments for the current user
    this.evaluatorAssignmentService.getAssignmentsByevaluator(this.userID).subscribe({
      next: (assignments) => {
        console.log('Evaluator assignments loaded:', assignments.length);

        if (assignments.length === 0) {
          // No assignments found
          this.outcomes = [];
          this.listItems = [];
          this.isLoading = false;
          return;
        }

        try {
          // Extract outcome IDs from assignments with proper error handling
          const outcomeIds = assignments
            .filter(assignment => assignment && assignment.subjectOutcome)
            .map(assignment => {
              if (!assignment.subjectOutcome.id) {
                console.warn('Assignment missing outcome ID:', assignment);
              }
              return assignment.subjectOutcome.id;
            })
            .filter(id => id); // Filter out any undefined/null IDs

          console.log('Found outcome IDs:', outcomeIds);

          if (outcomeIds.length === 0) {
            console.warn('No valid outcome IDs found in assignments');
            this.outcomes = [];
            this.listItems = [];
            this.isLoading = false;
            return;
          }

          // Create an array to store all outcome observables
          const outcomeObservables: Observable<any>[] = [];

          // For each assignment, get the corresponding outcome
          outcomeIds.forEach(outcomeId => {
            outcomeObservables.push(
              this.subjectOutcomeService.getOutcomeById(outcomeId)
            );
          });

          // Wait for all outcome requests to complete
          forkJoin(outcomeObservables).subscribe({
            next: (outcomesData) => {
              console.log('Outcomes loaded successfully:', outcomesData.length);
              this.outcomes = outcomesData;

              // Map outcomes to listItems format
              this.listItems = this.outcomes.map(outcome => ({
                id: outcome.id,  // Store the outcome ID directly
                title: this.truncateText(outcome.description),
                description: `${outcome.subjectName || ''} ${outcome.rubric?.description || 'Sin rúbrica'}`
              }));


              this.isLoading = false;

              setTimeout(() => {
                this.updateScrollButtons();
              }, 100);
            },
            error: (error) => {
              console.error('Error loading outcome details:', error);
              this.isLoading = false;
              this.listItems = [];
            }
          });
        } catch (err) {
          console.error('Error processing assignments:', err);
          this.isLoading = false;
          this.listItems = [];
        }
      },
      error: (error) => {
        console.error('Error loading evaluator assignments:', error);
        this.isLoading = false;
        this.listItems = [];
      }
    });
  }
  // Event handlers
  onMenuItemSelected(item: MenuItem): void {
    // Update active status of all menu items
    this.menuItems.forEach((menuItem) => {
      menuItem.active = menuItem.title === item.title;
    });

    // Call the appropriate load method based on the selected view
    if (item.title === 'RA a evaluar') {
      this.activeView = 'outcomes';
      this.loadOutcomes();
    } else {
      this.activeView = 'subjects';
      this.loadAsignatures();
    }
  }

  onSearch(value: string): void {
    this.wordSearch = value;
    this.filterAsignatures();
  }

  onScroll(): void {
    this.updateScrollButtons();
  }

  onItemClick(item: listItem): void {
    if (this.activeView === 'subjects') {
      // Find the corresponding asignature to get its ID
      const asignature = this.asignatures.find(a => a.name === item.title);
      if (asignature) {
        this.goToAsignature(asignature.id);
      }
    } else {
      console.log('Outcome item clicked:', item);

      // Use the stored ID directly if available
      if (item.id) {
        console.log('Navigating to outcome with ID:', item.id);
        this.goToOutcome(item.id);
      } else {
        // Fallback to the previous approach if no ID is stored
        const outcome = this.outcomes?.find(o => o.description === item.title);
        if (outcome) {
          console.log('Found outcome by description:', outcome);
          this.goToOutcome(outcome.id);
        } else {
          console.error('Could not find matching outcome for:', item.title);
        }
      }
    }
  }

  // Navigation methods
  goToAsignature(id: number): void {
    this.router.navigate(['/asignatures', id]);
  }


  goToOutcome( outcomeId: number): void {
    // Usar el ID real del outcome si está disponible

    this.router
      .navigate(['/home/subject/competency/subject/outcome'], {
        queryParams: {
          outcomeId: outcomeId,
          viewOnly: true
        },
      })
      .then(() => {
        console.log('Navegando al outcome:', outcomeId);
      })
      .catch((error) => {
        console.error('Error en la navegación:', error);
      });
  }
  // Utility methods
  private checkScreenSize() {
    this.isDesktop = window.innerWidth > 768; // 768px is typical medium breakpoint
  }

  private updateScrollButtons(): void {
    if (!this.carousel?.nativeElement) return;

    const container = this.carousel.nativeElement;
    const scrollLeft = container.scrollLeft;
    const maxScroll = container.scrollWidth - container.clientWidth;

    this.canScrollLeft = scrollLeft > 0;
    this.canScrollRight = scrollLeft < maxScroll;
  }

  filterAsignatures(): void {
    if (this.activeView === 'subjects') {
      if (!this.wordSearch) {
        this.asignaturesFilters = [...this.asignatures];
        this.listItems = this.asignatures.map(asignature => ({
          title: this.truncateText(asignature.name),
          description: asignature.description
        }));

      } else {
        const termino = this.wordSearch.toLowerCase();
        this.asignaturesFilters = this.asignatures.filter((asignature) =>
          asignature.name.toLowerCase().includes(termino)
        );

        // Update listItems based on filtered asignatures
        this.listItems = this.asignaturesFilters.map(asignature => ({
          title: this.truncateText(asignature.name),
          description: asignature.description
        }));

      }
    } else {
      // Handle filtering for outcomes view
      if (!this.wordSearch) {
        this.listItems = this.outcomes.map(outcome => ({
          id: outcome.id,
          title: this.truncateText(outcome.description),
          description: ` ${outcome.rubric?.description || 'Sin rubrica'}`
        }));


      } else {
        const termino = this.wordSearch.toLowerCase();
        const filteredOutcomes = this.outcomes.filter((outcome) =>
          outcome.description.toLowerCase().includes(termino)
        );
        this.listItems = filteredOutcomes.map(outcome => ({
          id: outcome.id,
          title: this.truncateText(outcome.description),
          description: `${outcome.rubric?.description || 'Sin rubrica'}`
        }));


      }
    }

    setTimeout(() => {
      this.updateScrollButtons();
    }, 100);
  }

  // Carousel controls
  scrollLeft(): void {
    if (!this.carousel?.nativeElement || !this.canScrollLeft) return;

    const container = this.carousel.nativeElement;
    const cardWidth = 320; // Ancho fijo de tarjeta + gap

    container.scrollBy({
      left: -cardWidth,
      behavior: 'smooth',
    });

    setTimeout(() => this.updateScrollButtons(), 300);
  }

  scrollRight(): void {
    if (!this.carousel?.nativeElement || !this.canScrollRight) return;

    const container = this.carousel.nativeElement;
    const cardWidth = 320; // Ancho fijo de tarjeta + gap

    container.scrollBy({
      left: cardWidth,
      behavior: 'smooth',
    });

    setTimeout(() => this.updateScrollButtons(), 300);
  }
}
