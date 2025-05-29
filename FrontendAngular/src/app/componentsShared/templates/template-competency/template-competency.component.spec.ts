import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateCompetencyComponent } from './template-competency.component';

describe('TemplateCompetencyComponent', () => {
  let component: TemplateCompetencyComponent;
  let fixture: ComponentFixture<TemplateCompetencyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateCompetencyComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateCompetencyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
