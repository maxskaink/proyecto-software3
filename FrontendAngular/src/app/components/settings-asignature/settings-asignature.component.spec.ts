import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsAsignatureComponent } from './settings-asignature.component';

describe('SettingsAsignatureComponent', () => {
  let component: SettingsAsignatureComponent;
  let fixture: ComponentFixture<SettingsAsignatureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SettingsAsignatureComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SettingsAsignatureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
