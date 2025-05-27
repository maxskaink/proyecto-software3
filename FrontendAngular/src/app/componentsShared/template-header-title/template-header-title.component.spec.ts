import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateHeaderTitleComponent } from './template-header-title.component';

describe('TemplateHeaderTitleComponent', () => {
  let component: TemplateHeaderTitleComponent;
  let fixture: ComponentFixture<TemplateHeaderTitleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateHeaderTitleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateHeaderTitleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
