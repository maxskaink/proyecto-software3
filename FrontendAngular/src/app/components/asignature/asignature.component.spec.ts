import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsignatureComponent } from './asignature.component';

describe('AsignatureComponent', () => {
  let component: AsignatureComponent;
  let fixture: ComponentFixture<AsignatureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsignatureComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AsignatureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
