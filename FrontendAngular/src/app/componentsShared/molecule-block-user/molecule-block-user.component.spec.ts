import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoleculeBlockUserComponent } from './molecule-block-user.component';

describe('MoleculeBlockUserComponent', () => {
  let component: MoleculeBlockUserComponent;
  let fixture: ComponentFixture<MoleculeBlockUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoleculeBlockUserComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MoleculeBlockUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
