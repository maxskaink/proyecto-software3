import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoleculeSearchBarComponent } from './molecule-search-bar.component';

describe('MoleculeSearchBarComponent', () => {
  let component: MoleculeSearchBarComponent;
  let fixture: ComponentFixture<MoleculeSearchBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoleculeSearchBarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MoleculeSearchBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
