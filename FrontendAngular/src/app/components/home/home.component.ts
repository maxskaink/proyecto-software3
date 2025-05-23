import { CommonModule, NgClass } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';

import { ActivatedRoute, Router, RouterLink} from '@angular/router';
import { title } from 'node:process';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { MoleculeSearchBarComponent } from '../../componentsShared/molecule-search-bar/molecule-search-bar.component';
import { MoleculeBlockComponent } from '../../componentsShared/molecule-block/molecule-block.component';
import { AsignatureDTO } from '../../models/SubjectDTO';
import { AsignatureService } from '../../services/asignature.service';


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

  asignatures: (AsignatureDTO & { titleColor: string; bodyColor: string })[] = [];
  asignaturesFilters: (AsignatureDTO & { titleColor: string; bodyColor: string })[] = [];
  wordSearch: string = '';
  @ViewChild('carousel', { static: false }) carousel!: ElementRef;


  constructor(
    private asignatureService: AsignatureService,
     private router: Router,    private route: ActivatedRoute) {}

  ngOnInit(): void {
    const colors2 = [
      { titleColor: '#60315C', bodyColor: '#8D538C' },
      { titleColor: '#3C866A', bodyColor: '#5E9E73' },
      { titleColor: '#4E4D6E', bodyColor: '#7474BB' }
    ];
    this.getAsignature(colors2);
    this.asignaturesFilters = [...this.asignatures];

 }
 getAsignature(colors2: any): void{
  this.asignatureService.getAsignature().subscribe(data => {
    this.asignatures = data.map(asignature => {
      const randomColor = colors2[Math.floor(Math.random() * colors2.length)];
      return {
        ...asignature,
        titleColor: randomColor.titleColor,
        bodyColor: randomColor.bodyColor
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
