import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup } from '@angular/forms';
import { BookingService } from '../services/booking.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-showtime-details-popup',
  templateUrl: './showtime-details-popup.component.html',
  styleUrls: ['./showtime-details-popup.component.scss']
})
export class ShowtimeDetailsPopupComponent {
  myForm: FormGroup;
  constructor(
    private router:Router,
    private BookingService:BookingService,
    public dialogRef: MatDialogRef<ShowtimeDetailsPopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.myForm = new FormGroup({
      noofticket: new FormControl(''),
      amount:new FormControl(''),
      showid: new FormControl('')
    });
  }
  ngOnDestroy(): void {
    this.router.navigate(['/user']);
    console.log("destroyer")
    this.BookingService.viewTickets()
  }
  noOninit(): void {
    
  }
  onCloseClick(): void {
    this.dialogRef.close();
    
  }
  calculateNet(): void {
    let cal = this.myForm.value;
    let ticketCount = cal.noofticket;
    let ticketPrice = 150;
    let Ticketamount = ticketCount * ticketPrice;
    if (ticketCount >= 5 && ticketCount <= 10) {
      Ticketamount -= Ticketamount * 0.08;
    } else if (ticketCount >= 21 && ticketCount <= 25) {
      Ticketamount -= Ticketamount * 0.2;
    }else if (ticketCount >= 11 && ticketCount <= 20){
      Ticketamount -= Ticketamount * 0.15;
    }
  
    this.myForm.controls['amount'].setValue(Ticketamount);
  }
  
  
  bookingticket(){
    let payload={
      showid: this.data.showtimeid,
      noofseats:this.myForm.value.noofticket,
      amount:this.myForm.value.amount
      }
      this.BookingService.booktickets(payload).subscribe(data=>{
        // this.showSuccessMessage();
        console.log(data)
      })
      setTimeout(() => {
        this.myForm.reset();
      }, 0);
      this.onCloseClick();
  }
}
