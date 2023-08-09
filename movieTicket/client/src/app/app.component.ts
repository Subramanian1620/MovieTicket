import { Component,  ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { BookingService } from './services/booking.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  @ViewChild('externalUrl', { static: true }) externalUrl!: ElementRef;
  title = 'angular-app';
  isAdmin: boolean = false;

  
  constructor(private router: Router,private BookingService: BookingService){}
  ngOnInit(): void {
    this.route()
  }
  route(){
    this.BookingService.validators().subscribe((resp: any) => {
      console.log(resp)
      if(resp==true){
        this.router.navigate(['/admin']);
      }
      // else if(resp.equals('login'))
      // {
      //   console.log("redirect")
      //   window.location.href = 'https://angular-60013485476.development.catalystserverless.in/__catalyst/auth/login';
      // }
      else{
        this.router.navigate(['/user']);
      }

    })
  }
  nag(){
    this.router.navigate(['/admin']);
  }
  nag1(){
    this.router.navigate(['/user']);
  }
  // login(){
  //   var redirectURL = "https://angular-60013485476.development.catalystserverless.in/__catalyst/auth/login";
  //       var auth = catalyst.auth;
  //       auth.signOut(redirectURL); 
  // }

}

