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


  private truncateText(text: string, maxLength: number = 50): string {
    if (!text) return '';
    return text.length > maxLength
      ? text.substring(0, maxLength) + '...'
      : text;
  }

  // Lifecycle methods
  ngOnInit(): void {
    this.authService.uid.subscribe((uid) => {
      if (uid) {
        this.userID = uid;
      } else {
        console.error('User ID not available');
      }
    });
    this.loadAsignatures();
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

    // First, check if we have a valid user ID
    if (!this.userID) {
      console.error('Cannot load outcomes: User ID not available');
      this.isLoading = false;
      this.listItems = [];
      return;
    }

    // Get evaluator assignments for the current user
    this.evaluatorAssignmentService.getAssignmentsByevaluator(this.userID).subscribe({
      next: (assignments) => {
        console.log('Evaluator assignments loaded:', assignments.length);
        console.log('Assignments:', assignments);

        if (assignments.length === 0) {
          // No assignments found
          this.outcomes = [];
          this.listItems = [];
          this.isLoading = false;
          return;
        }

        // Extract outcome IDs from assignments
        const outcomeIds = assignments.map(assignment => assignment.subjectOutcome.id);


        // Create an array to store all outcome promises
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
            this.outcomes = outcomesData;

            // Map outcomes to listItems format
            this.listItems = this.outcomes.map(outcome => ({
              title: this.truncateText(outcome.description),
              description: `${outcome.rubric?.description || 'Sin rúbrica'}`
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
      // Find the corresponding outcome to get its ID
      const outcome = this.outcomes?.find(o => o.description === item.title);
      if (outcome) {
        this.goToOutcome(outcome.id);
      }
    }
  }

  // Navigation methods
  goToAsignature(id: number): void {
    this.router.navigate(['/asignatures', id]);
  }

  goToOutcome(id: number): void {
    this.router.navigate(['/outcomes', id]);
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
          title: this.truncateText(outcome.description),
          description: ` ${outcome.rubric?.description || 'Sin rubrica'}`
        }));


      } else {
        const termino = this.wordSearch.toLowerCase();
        const filteredOutcomes = this.outcomes.filter((outcome) =>
          outcome.description.toLowerCase().includes(termino)
        );

        this.listItems = filteredOutcomes.map(outcome => ({
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
