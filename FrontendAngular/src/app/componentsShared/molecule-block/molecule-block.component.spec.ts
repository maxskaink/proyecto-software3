import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoleculeBlockComponent } from './molecule-block.component';

describe('MoleculeBlockComponent', () => {
  let component: MoleculeBlockComponent;
  let fixture: ComponentFixture<MoleculeBlockComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoleculeBlockComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MoleculeBlockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
