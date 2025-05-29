import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateCompetencyEditComponent } from './template-competency-edit.component';

describe('TemplateCompetencyEditComponent', () => {
  let component: TemplateCompetencyEditComponent;
  let fixture: ComponentFixture<TemplateCompetencyEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateCompetencyEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateCompetencyEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
