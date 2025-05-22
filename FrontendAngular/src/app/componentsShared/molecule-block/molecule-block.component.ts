import { CommonModule, isPlatformBrowser } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, ElementRef, Inject, Input, PLATFORM_ID, ViewChild } from '@angular/core';
import { SrvRecord } from 'node:dns';

@Component({
  selector: 'app-molecule-block',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './molecule-block.component.html',
  styleUrl: './molecule-block.component.css'
})
export class MoleculeBlockComponent {
  @ViewChild('titleElement', { static: false }) titleElement!: ElementRef;

  @Input() titleColor: string = '#cccccc';
  @Input() bodyColor: string= '#cddddd';
  @Input() title: string = '';
  @Input() description: string = '';
  @Input() subTitle: string = '';
  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

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
