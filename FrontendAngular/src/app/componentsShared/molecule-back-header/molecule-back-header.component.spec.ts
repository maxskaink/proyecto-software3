import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoleculeBackHeaderComponent } from './molecule-back-header.component';

describe('MoleculeBackHeaderComponent', () => {
  let component: MoleculeBackHeaderComponent;
  let fixture: ComponentFixture<MoleculeBackHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoleculeBackHeaderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MoleculeBackHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
