import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateListboxCompleteComponent } from './template-listbox-complete.component';

describe('TemplateListboxCompleteComponent', () => {
  let component: TemplateListboxCompleteComponent;
  let fixture: ComponentFixture<TemplateListboxCompleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateListboxCompleteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateListboxCompleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
