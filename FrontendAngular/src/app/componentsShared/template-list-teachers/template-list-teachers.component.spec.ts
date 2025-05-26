import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateListTeachersComponent } from './template-list-teachers.component';

describe('TemplateListTeachersComponent', () => {
  let component: TemplateListTeachersComponent;
  let fixture: ComponentFixture<TemplateListTeachersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateListTeachersComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateListTeachersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
