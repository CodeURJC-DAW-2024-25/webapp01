import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddListPopupComponent } from './add-to-list-popup.component';

describe('AddToListPopupComponent', () => {
  let component: AddListPopupComponent;
  let fixture: ComponentFixture<AddListPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddListPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddListPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
