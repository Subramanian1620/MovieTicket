import { Component, OnInit, Inject } from '@angular/core';
import { BookingService } from 'src/app/services/booking.service';
import { FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-shows',
  templateUrl: './shows.component.html',
  styleUrls: ['./shows.component.scss']
})
export class ShowsComponent implements OnInit {
  myForm: FormGroup;
  constructor( private router:Router,
    private BookingService:BookingService,
    public dialogRef: MatDialogRef<ShowsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    this.myForm = new FormGroup({ });
  }
  


  ngOnInit(): void {
   
  }
  deleteshows(): void {
    let payload = {
      showid: this.data.showtimeid // Use the showid parameter
    };
      this.BookingService.cancelshows(payload).subscribe(data=>{
        console.log(data)
       
      })
      this.onCloseClick()
  }
  
  onCloseClick(): void {
    this.dialogRef.close();
    
  }
}
