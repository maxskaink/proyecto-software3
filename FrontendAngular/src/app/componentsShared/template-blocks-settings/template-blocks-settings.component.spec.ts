import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateBlocksSettingsComponent } from './template-blocks-settings.component';

describe('TemplateBlocksSettingsComponent', () => {
  let component: TemplateBlocksSettingsComponent;
  let fixture: ComponentFixture<TemplateBlocksSettingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateBlocksSettingsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateBlocksSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
