import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertmessageComponent } from './alertmessage.component';

describe('AlertmessageComponent', () => {
  let component: AlertmessageComponent;
  let fixture: ComponentFixture<AlertmessageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertmessageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlertmessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
