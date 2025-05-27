import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateGridCompetencyProgramComponent } from './template-grid-competency-program.component';

describe('TemplateGridCompetencyProgramComponent', () => {
  let component: TemplateGridCompetencyProgramComponent;
  let fixture: ComponentFixture<TemplateGridCompetencyProgramComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateGridCompetencyProgramComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateGridCompetencyProgramComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
