import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateCreateRubricTableComponent } from './template-create-rubric-table.component';

describe('TemplateCreateRubricTableComponent', () => {
  let component: TemplateCreateRubricTableComponent;
  let fixture: ComponentFixture<TemplateCreateRubricTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateCreateRubricTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateCreateRubricTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
