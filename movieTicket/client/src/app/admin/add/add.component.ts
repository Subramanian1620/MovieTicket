import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { BookingService } from 'src/app/services/booking.service';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { ShowsComponent } from '../shows/shows.component';
import { MatDialog } from '@angular/material/dialog';
@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss']
})
export class AddComponent implements OnInit {
  displayedColumns: string[] = ['moviename', 'theatresname', 'showtime','totalseats','amount', 'actions'];
  myForm: FormGroup;
  myForm1: FormGroup;
  myForm5: FormGroup;
  movieslist: any;
  theatreslist: any;
  deleteshows: any;
  curDate: string;

  constructor(private BookingService:BookingService,private dialog: MatDialog,
    private router: Router) {
    this.myForm = new FormGroup({
      username: new FormControl(''),
      password:new FormControl(''),
      totalseats:new FormControl(''),
      upload: new FormControl('')
    });
    this.myForm1 = new FormGroup({
      moviename: new FormControl(''),
      upload: new FormControl('')
    });
    this.myForm5 = new FormGroup({
      moviename: new FormControl(''),
      theatresname: new FormControl(''),
      showDate: new FormControl(''),
      hours: new FormControl(''),
      minutes:new FormControl(''),
      seconds:new FormControl(''),
    });
    this.curDate = new Date().toISOString().slice(0, 16);
  }
  ngOnInit(): void {
    // // this.getmovielist()
    // this.gettheatreslist()
    // this.deleteshowtime()
  }
  onTabChange(event: MatTabChangeEvent) {
    if (event.index === 2) {
      console.log("Tab index is 2");
      this.getmovielist();
      this.gettheatreslist();
    }
    if (event.index === 3) {
      console.log("Tab index is 3");
      this.deleteshowtime()
    }
  }
  addTheatres(){
    let payload={
      theatresname: this.myForm.value.username,
      location:this.myForm.value.password,
      totalseats:this.myForm.value.totalseats,
      upload:this.myForm.value.upload,
      }
      this.BookingService.getcreatetheatres(payload).subscribe(data=>{
  
        console.log(data)
      })
        this.myForm.reset();
        this.gettheatreslist()
  }
  addmovie(){
    let payload={
      moviename: this.myForm1.value.moviename
      }
      this.BookingService.getcreatemovie(payload).subscribe(data=>{
        console.log(data)
      })
        this.myForm1.reset();
        // this.getmovielist()
  }
  nag(){
    this.router.navigate(['/showstiming']);
  }
  getmovielist(): void {
    this.BookingService.getmovielist().subscribe((resp: any) => {
      console.log(resp)
      this.movieslist = resp;
      console.log(this.movieslist);
    })
  }
  onclick(){
    this.getmovielist()
  }
  gettheatreslist(): void {
    this.BookingService.gettheatreslist().subscribe((resp: any) => {
      console.log(resp)
      this.theatreslist = resp;
      console.log(this.theatreslist);
    })
  }
  addshowtimes(){
    console.log(this.myForm.value.theatresname,this.myForm.value.moviename)
    let payload={
      showtime: moment(this.myForm5.value.showDate).format("YYYY-MM-DD HH:mm:ss"),
      theatresid:this.myForm5.value.theatresname,
      movieid:this.myForm5.value.moviename
      }
      this.BookingService.getaddshowtimings(payload).subscribe(data=>{
        
        // console.log(data)
      })
      this.myForm5.reset();
      this.deleteshowtime();
  }
  deleteshowtime(): void {
    this.BookingService.deleteShowTimes().subscribe((resp: any) => {
      console.log(resp)
      this.deleteshows = resp;
      console.log(this.deleteshows);

    })
  }
  openPopup(showid: any): void {
    const dialogRef = this.dialog.open(ShowsComponent, {
      width: '200px',
      height: '150px',
      data: showid
    });
    dialogRef.afterClosed().subscribe(() => {
      this.deleteshowtime()
      setTimeout(() => {
      }, 1500);
    });

  }
}
