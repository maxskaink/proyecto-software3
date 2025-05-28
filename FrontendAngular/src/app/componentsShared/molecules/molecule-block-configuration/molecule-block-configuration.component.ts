import { CommonModule } from '@angular/common';
import { Component, Inject, Input, PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-molecule-block-configuration',
  imports: [CommonModule],
  templateUrl: './molecule-block-configuration.component.html',
  styleUrl: './molecule-block-configuration.component.css'
})
export class MoleculeBlockConfigurationComponent {

  @Input() title: string = '';
  @Input() description: string = '';
  @Input() route: string='';
  constructor(@Inject(PLATFORM_ID) private platformId: Object, private router: Router) {}
  navigateToRoute() {
    if (this.route) {
      this.router.navigate([this.route]);
    }
  }
}
