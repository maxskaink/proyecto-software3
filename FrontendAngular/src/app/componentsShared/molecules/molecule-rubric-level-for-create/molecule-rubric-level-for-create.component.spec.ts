import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoleculeRubricLevelForCreateComponent } from './molecule-rubric-level-for-create.component';

describe('MoleculeRubricLevelForCreateComponent', () => {
  let component: MoleculeRubricLevelForCreateComponent;
  let fixture: ComponentFixture<MoleculeRubricLevelForCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoleculeRubricLevelForCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MoleculeRubricLevelForCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
