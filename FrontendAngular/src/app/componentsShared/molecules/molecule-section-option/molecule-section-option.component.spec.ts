import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoleculeSectionOptionComponent } from './molecule-section-option.component';

describe('MoleculeSectionOptionComponent', () => {
  let component: MoleculeSectionOptionComponent;
  let fixture: ComponentFixture<MoleculeSectionOptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoleculeSectionOptionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MoleculeSectionOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
