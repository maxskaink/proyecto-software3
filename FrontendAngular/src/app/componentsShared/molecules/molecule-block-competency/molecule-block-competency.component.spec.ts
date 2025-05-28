import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoleculeBlockCompetencyComponent } from './molecule-block-competency.component';

describe('MoleculeBlockCompetencyComponent', () => {
  let component: MoleculeBlockCompetencyComponent;
  let fixture: ComponentFixture<MoleculeBlockCompetencyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoleculeBlockCompetencyComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MoleculeBlockCompetencyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
