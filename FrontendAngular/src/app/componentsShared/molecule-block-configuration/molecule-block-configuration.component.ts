import { CommonModule } from '@angular/common';
import { Component, Inject, Input, PLATFORM_ID } from '@angular/core';

@Component({
  selector: 'app-molecule-block-configuration',
  imports: [CommonModule],
  templateUrl: './molecule-block-configuration.component.html',
  styleUrl: './molecule-block-configuration.component.css'
})
export class MoleculeBlockConfigurationComponent {

  @Input() title: string = '';
  @Input() description: string = '';
  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  
}
