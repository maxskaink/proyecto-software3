import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoleculeLevelBoxComponent } from './molecule-level-box.component';

describe('MoleculeLevelBoxComponent', () => {
  let component: MoleculeLevelBoxComponent;
  let fixture: ComponentFixture<MoleculeLevelBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoleculeLevelBoxComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MoleculeLevelBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
