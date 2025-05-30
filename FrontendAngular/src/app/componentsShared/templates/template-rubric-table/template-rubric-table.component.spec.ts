import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateRubricTableComponent } from './template-rubric-table.component';

describe('TemplateRubricTableComponent', () => {
  let component: TemplateRubricTableComponent;
  let fixture: ComponentFixture<TemplateRubricTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateRubricTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateRubricTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
