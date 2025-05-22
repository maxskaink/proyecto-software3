import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoleculueCompetencySectionComponent } from './moleculue-competency-section.component';

describe('MoleculueCompetencySectionComponent', () => {
  let component: MoleculueCompetencySectionComponent;
  let fixture: ComponentFixture<MoleculueCompetencySectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoleculueCompetencySectionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MoleculueCompetencySectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
