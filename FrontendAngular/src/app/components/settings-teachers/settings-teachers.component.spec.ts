import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsTeachersComponent } from './settings-teachers.component';

describe('SettingsTeachersComponent', () => {
  let component: SettingsTeachersComponent;
  let fixture: ComponentFixture<SettingsTeachersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SettingsTeachersComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SettingsTeachersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
