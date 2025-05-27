import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';

interface MenuItem {
  title: string;
  action?: () => void; // Optional callback function for each item
}

@Component({
  selector: 'app-molecule-aside-menu',
  imports: [CommonModule],
  templateUrl: './molecule-aside-menu.component.html',
  styleUrl: './molecule-aside-menu.component.css'
})
export class MoleculeAsideMenuComponent {
  @Input() menuItems: MenuItem[] = [];
  @Output() itemSelected = new EventEmitter<MenuItem>();

  onItemClick(item: MenuItem): void {
    // Execute the specific action of the item if it exists
    if (item.action) {
      item.action();
    }
    
    // Also emit the event to maintain compatibility
    this.itemSelected.emit(item);
  }
}