import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateRubricCreateCriterionComponent } from './template-rubric-create-criterion.component';

describe('TemplateRubricCreateCriterionComponent', () => {
  let component: TemplateRubricCreateCriterionComponent;
  let fixture: ComponentFixture<TemplateRubricCreateCriterionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateRubricCreateCriterionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateRubricCreateCriterionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
