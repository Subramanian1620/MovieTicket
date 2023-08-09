import { Component, OnInit } from '@angular/core';
import { BookingService } from 'src/app/services/booking.service';
import { FormControl, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ShowtimeDetailsPopupComponent } from 'src/app/showtime-details-popup/showtime-details-popup.component';
import { Router } from '@angular/router';
import { MatTabChangeEvent } from '@angular/material/tabs';

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.scss']
})
export class BookingComponent implements OnInit {
  displayedColumns: string[] = ['moviename', 'theatresname', 'showtime', 'availableSeats', 'actions'];
  displayedColumns1: string[] = ['moviename', 'theatresname', 'showtime', 'noofseats'];
  divisionList: any[] = [];
  divisionList1: any;
  myForm: FormGroup;
  movieData: any;

  constructor(private BookingService: BookingService, private dialog: MatDialog, private router: Router) {
    this.myForm = new FormGroup({
      moviename: new FormControl(''),
      theatresname: new FormControl(''),
      noofseats: new FormControl('')
    });
  }

  ngOnInit(): void {
    this.getShowtimes();
  }
  getShowtimes(): void {
    this.BookingService.getshowtime().subscribe((resp: any) => {
      this.divisionList = resp;
      console.log(this.divisionList);
    });
  }
  openPopup(showid: any): void {
    const dialogRef = this.dialog.open(ShowtimeDetailsPopupComponent, {
      width: '650px',
      height: '550px',
      data: showid
    });
    dialogRef.afterClosed().subscribe(() => {
      // this.getShowtimes();
      setTimeout(() => {
        this.getShowtimes();
      }, 1500);
    });

  }
  loaded(){
   
  }
  onTabChange(event: MatTabChangeEvent) {
    if (event.index === 1) {
      this.gettheatreslist();
    }
    if (event.index === 0) {
      this.getShowtimes();
    }
  }
  gettheatreslist(): void {
    console.log('asdf')
    this.BookingService.viewTickets().subscribe((resp: any) => {
      this.movieData = resp;

    })
  }
  navi() {
    this.router.navigate(['/viewuser']);
  }
}
