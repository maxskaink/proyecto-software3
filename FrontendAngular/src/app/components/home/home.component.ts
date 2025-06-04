import { CommonModule, NgClass } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink} from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import { MoleculeSearchBarComponent } from '../../componentsShared/molecules/molecule-search-bar/molecule-search-bar.component';
import { MoleculeBlockComponent } from '../../componentsShared/molecules/molecule-block/molecule-block.component';
import { SubjectDTO } from '../../models/SubjectDTO';
import { SubjectService } from '../../services/subject.service';
import { LoadingComponent } from '../../componentsShared/loading/loading.component';
import { AlertmessageComponent } from '../../componentsShared/messages/alertmessage/alertmessage.component';


@Component({
    selector: 'app-home',
    imports: [
        HeaderComponent,
        MoleculeBlockComponent,
        MoleculeSearchBarComponent,
        CommonModule,
        FormsModule,
        LoadingComponent,
        AlertmessageComponent
    ],
    templateUrl: './home.component.html',
    styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit, AfterViewInit {

  asignatures: (SubjectDTO)[] = [];
  asignaturesFilters: (SubjectDTO)[] = [];
  wordSearch: string = '';
  isLoading: boolean = true;
  
  @ViewChild('carousel', { static: false }) carousel!: ElementRef;
  
  canScrollLeft = false;
  canScrollRight = true;

  constructor(
    private asignatureService: SubjectService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadAsignatures();
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.updateScrollButtons();
    }, 200);
  }

  private loadAsignatures(): void {
    this.isLoading = true;
    this.asignatureService.getAssignedSubject().subscribe({
      next: (data) => {
        this.asignatures = data;
        this.asignaturesFilters = [...this.asignatures];
        
        this.isLoading = false;
        
        setTimeout(() => {
          this.updateScrollButtons();
        }, 100);
      },
      error: (error) => {
        console.error('Error loading subjects:', error);
        this.isLoading = false;
      }
    });
  }

  private updateScrollButtons(): void {
    if (!this.carousel?.nativeElement) return;
    
    const container = this.carousel.nativeElement;
    const scrollLeft = container.scrollLeft;
    const maxScroll = container.scrollWidth - container.clientWidth;
    
    this.canScrollLeft = scrollLeft > 0;
    this.canScrollRight = scrollLeft < maxScroll;
  }

  scrollLeft(): void {
    if (!this.carousel?.nativeElement || !this.canScrollLeft) return;
    
    const container = this.carousel.nativeElement;
    const cardWidth = 320; // Ancho fijo de tarjeta + gap
    
    container.scrollBy({ 
      left: -cardWidth, 
      behavior: 'smooth' 
    });
    
    setTimeout(() => this.updateScrollButtons(), 300);
  }

  scrollRight(): void {
    if (!this.carousel?.nativeElement || !this.canScrollRight) return;
    
    const container = this.carousel.nativeElement;
    const cardWidth = 320; // Ancho fijo de tarjeta + gap
    
    container.scrollBy({ 
      left: cardWidth, 
      behavior: 'smooth' 
    });
    
    setTimeout(() => this.updateScrollButtons(), 300);
  }

  filterAsignatures(): void {
    if (!this.wordSearch) {
      this.asignaturesFilters = [...this.asignatures];
    } else {
      const termino = this.wordSearch.toLowerCase();
      this.asignaturesFilters = this.asignatures.filter(asignature =>
        asignature.name.toLowerCase().includes(termino)
      );
    }
    
    setTimeout(() => {
      this.updateScrollButtons();
    }, 100);
  }

  onSearch(value: string): void {
    this.wordSearch = value;
    this.filterAsignatures();
  }

  goToAsignature(id: number): void {
    this.router.navigate(['/asignatures', id]);
  }

  // Escuchar scroll manual para actualizar botones
  onScroll(): void {
    this.updateScrollButtons();
  }
}