import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoleculeBlockConfigurationComponent } from './molecule-block-configuration.component';

describe('MoleculeBlockConfigurationComponent', () => {
  let component: MoleculeBlockConfigurationComponent;
  let fixture: ComponentFixture<MoleculeBlockConfigurationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoleculeBlockConfigurationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MoleculeBlockConfigurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
