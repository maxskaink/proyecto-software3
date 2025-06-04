import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CorrectSaveComponent } from './correct-save.component';

describe('CorrectSaveComponent', () => {
  let component: CorrectSaveComponent;
  let fixture: ComponentFixture<CorrectSaveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CorrectSaveComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CorrectSaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
