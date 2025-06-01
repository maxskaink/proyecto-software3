import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, ElementRef, Inject, Input, PLATFORM_ID, ViewChild } from '@angular/core';

// Define color pairs as a constant outside the class
const COLOR_PAIRS = [
  { titleColor: '#6C3668', bodyColor: '#8D538C' },
  { titleColor: '#3C866A', bodyColor: '#5E9E73' }, //verde
  { titleColor: '#4E4D6E', bodyColor: '#7474BB' },
  { titleColor: '#A65256', bodyColor: '#CD676C' }, //rojo
];

@Component({
  selector: 'app-molecule-block',
  imports: [CommonModule],
  templateUrl: './molecule-block.component.html',
  styleUrl: './molecule-block.component.css'
})
export class MoleculeBlockComponent {
  @ViewChild('titleElement', { static: false }) titleElement!: ElementRef<HTMLElement>;

  @Input() title: string = '';
  @Input() description: string = '';
  @Input() subtitle: string = '';
  
  titleColor: string = '';
  bodyColor: string = '';

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    const colors = this.getColorPairForName(this.title);
    this.titleColor = colors.titleColor;
    this.bodyColor = colors.bodyColor;
  }

  ngAfterViewInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.adjustFontSize();
    }
  }

  private getColorPairForName(name: string): {titleColor: string, bodyColor: string} {
    const hash = this.hashString(name);
    const index = hash % COLOR_PAIRS.length;
    return COLOR_PAIRS[index];
  }

  private hashString(str: string): number {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
      hash = str.charCodeAt(i) + ((hash << 5) - hash);
      hash = hash & hash; // Convert to 32 bit integer
    }
    return Math.abs(hash);
  }

  private adjustFontSize(): void {
    const el = this.titleElement.nativeElement;
    const maxFontSize = 35;
    const minFontSize = 10;
    const decrementStep = 7;
    
    let fontSize = maxFontSize;
    el.style.fontSize = `${fontSize}px`;

    // Reduce font size until text fits or minimum size reached
    while (el.scrollWidth > el.offsetWidth && fontSize > minFontSize) {
      fontSize -= decrementStep;
      el.style.fontSize = `${fontSize}px`;
    }

    // If text fits with original size, reset to original
    if (el.scrollWidth <= el.offsetWidth && fontSize < maxFontSize) {
      el.style.fontSize = `${maxFontSize}px`;
    }
  }
}