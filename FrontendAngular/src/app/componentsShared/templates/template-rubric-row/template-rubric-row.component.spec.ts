import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateRubricRowComponent } from './template-rubric-row.component';

describe('TemplateRubricRowComponent', () => {
  let component: TemplateRubricRowComponent;
  let fixture: ComponentFixture<TemplateRubricRowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateRubricRowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateRubricRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
