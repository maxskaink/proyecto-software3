import { CommonModule, NgClass } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';

import { ActivatedRoute, Router, RouterLink} from '@angular/router';
import { title } from 'node:process';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { MoleculeSearchBarComponent } from '../../componentsShared/molecules/molecule-search-bar/molecule-search-bar.component';
import { MoleculeBlockComponent } from '../../componentsShared/molecules/molecule-block/molecule-block.component';
import { SubjectDTO } from '../../models/SubjectDTO';
import { AsignatureService } from '../../services/subject.service';


@Component({
    selector: 'app-home',
    imports: [
        HeaderComponent,
        FooterComponent,
        MoleculeBlockComponent,
        MoleculeSearchBarComponent,
        CommonModule,
        FormsModule
    ],
    templateUrl: './home.component.html',
    styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{

  asignatures: (SubjectDTO )[] = [];
  asignaturesFilters: (SubjectDTO )[] = [];
  wordSearch: string = '';
  @ViewChild('carousel', { static: false }) carousel!: ElementRef;


  constructor(
    private asignatureService: AsignatureService,
     private router: Router,    private route: ActivatedRoute) {}

  ngOnInit(): void {

    this.getAsignature();
    this.asignaturesFilters = [...this.asignatures];

 }
 getAsignature(): void{
  this.asignatureService.getAsignature().subscribe(data => {
    this.asignatures = data.map(asignature => {     
      return {
        ...asignature,
      };
    });
  });
 }
  filterAsignatures(): void {
    if (!this.wordSearch) {
      this.asignaturesFilters = [...this.asignatures];
    } else {
      const termino = this.wordSearch.toLowerCase();
      this.asignaturesFilters= this.asignatures.filter(asignature =>
        asignature.title.toLowerCase().includes(termino)
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
