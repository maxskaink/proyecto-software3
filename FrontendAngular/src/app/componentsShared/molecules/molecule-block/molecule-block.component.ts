import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, ElementRef, Inject, Input, PLATFORM_ID, ViewChild } from '@angular/core';

// Define color pairs as a constant outside the class
const COLOR_PAIRS = [
  { titleColor: '#6C3668', bodyColor: '#8D538C' },
  { titleColor: '#3C866A', bodyColor: '#5E9E73' }, //verde
  { titleColor: '#4E4D6E', bodyColor: '#7474BB' },
  { titleColor: '#A65256', bodyColor: '#CD676C' }, //rojo
];
const HOVER_COLORS = [
  { titleColor: '#553C53', bodyColor: '#6C3668' },
  { titleColor: '#3C866A', bodyColor: '#3C866A' }, //verde
  { titleColor: '#5647AB', bodyColor: '#4E4D6E' },
  { titleColor: '#B83238', bodyColor: '#A65256' }, /// Para el par rojo
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
  @Input() state: 'primary' | 'secondary' = 'primary';
  titleColor: string = '';
  bodyColor: string = '';
  hoverColors: { titleColor: string, bodyColor: string } | null = null;
  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    if(this.state=='primary') {
      const colors = this.getColorPairForName(this.title);
      this.titleColor = colors.titleColor;
      this.bodyColor = colors.bodyColor;
      this.hoverColors = colors.hoverColors;
    }
  }

  ngAfterViewInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      
    }
  }

  private getColorPairForName(name: string): {titleColor: string, bodyColor: string, hoverColors: typeof HOVER_COLORS[0]} {
    const hash = this.hashString(name);
    const index = hash % COLOR_PAIRS.length;
    return {
      ...COLOR_PAIRS[index],
      hoverColors: HOVER_COLORS[index]
    };
  }

  private hashString(str: string): number {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
      hash = str.charCodeAt(i) + ((hash << 5) - hash);
      hash = hash & hash; // Convert to 32 bit integer
    }
    return Math.abs(hash);
  }


}