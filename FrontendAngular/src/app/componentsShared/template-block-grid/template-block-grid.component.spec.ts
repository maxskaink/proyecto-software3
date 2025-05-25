import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateBlockGridComponent } from './template-block-grid.component';

describe('TemplateBlockGridComponent', () => {
  let component: TemplateBlockGridComponent;
  let fixture: ComponentFixture<TemplateBlockGridComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateBlockGridComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateBlockGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
