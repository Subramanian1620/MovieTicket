import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowtimeDetailsPopupComponent } from './showtime-details-popup.component';

describe('ShowtimeDetailsPopupComponent', () => {
  let component: ShowtimeDetailsPopupComponent;
  let fixture: ComponentFixture<ShowtimeDetailsPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowtimeDetailsPopupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowtimeDetailsPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
