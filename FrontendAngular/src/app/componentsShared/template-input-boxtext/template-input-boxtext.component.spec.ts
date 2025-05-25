import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateInputBoxtextComponent } from './template-input-boxtext.component';

describe('TemplateInputBoxtextComponent', () => {
  let component: TemplateInputBoxtextComponent;
  let fixture: ComponentFixture<TemplateInputBoxtextComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateInputBoxtextComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateInputBoxtextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
