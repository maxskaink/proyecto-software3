import { CommonModule, NgClass } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';

import { ActivatedRoute, Router, RouterLink} from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import { MoleculeSearchBarComponent } from '../../componentsShared/molecules/molecule-search-bar/molecule-search-bar.component';
import { MoleculeBlockComponent } from '../../componentsShared/molecules/molecule-block/molecule-block.component';
import { SubjectDTO } from '../../models/SubjectDTO';
import { SubjectService } from '../../services/subject.service';
import { LoadingComponent } from '../../componentsShared/loading/loading.component';


@Component({
    selector: 'app-home',
    imports: [
        HeaderComponent,
        MoleculeBlockComponent,
        MoleculeSearchBarComponent,
        CommonModule,
        FormsModule,
        LoadingComponent
    ],
    templateUrl: './home.component.html',
    styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{

  asignatures: (SubjectDTO )[] = [];
  asignaturesFilters: (SubjectDTO )[] = [];
  wordSearch: string = '';
  isLoading: boolean = true;
  @ViewChild('carousel', { static: false }) carousel!: ElementRef;


  constructor(
    private asignatureService: SubjectService,
     private router: Router,    private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.loadAsignatures();
 }
 
  private loadAsignatures(): void {
    this.isLoading = true; // Ensure loading is true when starting
    this.asignatureService.getAssignedSubject().subscribe({
      next: (data) => {
        this.asignatures = data;
        this.asignaturesFilters = [...this.asignatures];
        this.isLoading = false; // Hide loading only after data is loaded
      },
      error: (error) => {
        console.error('Error loading subjects:', error);
        this.isLoading = false; // Hide loading on error too
      }
    });
  }
  filterAsignatures(): void {
    if (!this.wordSearch) {
      this.asignaturesFilters = [...this.asignatures];
    } else {
      const termino = this.wordSearch.toLowerCase();
      this.asignaturesFilters= this.asignatures.filter(asignature =>
        asignature.name.toLowerCase().includes(termino)
      );
    }
  }
  onSearch(value: string): void {
    this.wordSearch = value;
    this.filterAsignatures();
  }

  goToAsignature(id: number): void {
    this.router.navigate(['/asignatures', id]);
  }
  scrollLeft(): void {
    console.log('Right clicked');
    this.carousel.nativeElement.scrollBy({ left: -260, behavior: 'smooth' });
  }

  scrollRight(): void {
    console.log('Right clicked');
    this.carousel.nativeElement.scrollBy({ left: 260, behavior: 'smooth' });
  }
}
