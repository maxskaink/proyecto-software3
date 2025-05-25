import { CommonModule, isPlatformBrowser } from '@angular/common';
import {} from '@angular/common/http';
import { Component, ElementRef, Inject, Input, PLATFORM_ID, ViewChild } from '@angular/core';
import { SrvRecord } from 'node:dns';

@Component({
    selector: 'app-molecule-block',
    imports: [CommonModule],
    templateUrl: './molecule-block.component.html',
    styleUrl: './molecule-block.component.css'
})
export class MoleculeBlockComponent {
  @ViewChild('titleElement', { static: false }) titleElement!: ElementRef;

  @Input() title: string = '';
  @Input() description: string = '';
  @Input() subTitle: string = '';
  titleColor: string = '';
  bodyColor: string = '';
  ngOnInit() {
    const colors = this.getColorPairForName(this.title);
    this.titleColor = colors.titleColor;
    this.bodyColor = colors.bodyColor;
  }
  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}
  COLOR_PAIRS = [
    { titleColor: '#6C3668 ', bodyColor: '#8D538C' },
    { titleColor: '#3C866A', bodyColor: '#5E9E73' },
    { titleColor: '#4E4D6E', bodyColor: '#7474BB' },
    { titleColor: '#A65256', bodyColor: '#CD676C' },
  ];
  hashString(str: string): number {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
      hash = str.charCodeAt(i) + ((hash << 5) - hash);
      hash = hash & hash; // Convierte a 32 bits
    }
    return Math.abs(hash);
  }
  getColorPairForName(name: string) {
    const hash = this.hashString(name);
    const index = hash % this.COLOR_PAIRS.length;
    return this.COLOR_PAIRS[index];
  }
  ngAfterViewInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.adjustFontSize();
    }
  }

  adjustFontSize(): void {
    const el = this.titleElement.nativeElement as HTMLElement;
    let fontSize = 35;
    el.style.fontSize = `${fontSize}px`;
    const originalFontSize = fontSize;

    while (el.scrollWidth > el.offsetWidth && fontSize > 10) {
      fontSize -= 7;
      el.style.fontSize = `${fontSize}px`;
    }

    if (el.scrollWidth <= el.offsetWidth && fontSize < originalFontSize) {
      el.style.fontSize = `${originalFontSize}px`;
    }
  }

}
