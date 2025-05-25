import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsCompProgramComponent } from './settings-comp-program.component';

describe('SettingsCompProgramComponent', () => {
  let component: SettingsCompProgramComponent;
  let fixture: ComponentFixture<SettingsCompProgramComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SettingsCompProgramComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SettingsCompProgramComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
