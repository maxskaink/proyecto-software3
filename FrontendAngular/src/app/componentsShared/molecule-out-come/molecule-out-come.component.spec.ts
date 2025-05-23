import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoleculeOutComeComponent } from './molecule-out-come.component';

describe('MoleculeOutComeComponent', () => {
  let component: MoleculeOutComeComponent;
  let fixture: ComponentFixture<MoleculeOutComeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoleculeOutComeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MoleculeOutComeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
