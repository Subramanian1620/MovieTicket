import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
    baseURL = "https://angular-60013485476.development.catalystserverless.in/server/createmovie/";
      // baseURL = "http://localhost:3000/server/createmovie/";

  constructor(private http: HttpClient) { }

  getcreatemovie(data: any): Observable<any> {
    const url = this.baseURL + 'createmovie?moviename=' + data.moviename;
    return this.http.get(url);
  }
  getcreatetheatres(data: any): Observable<any> {
    const url = this.baseURL + 'addtheatres?theatresname=' + data.theatresname + '&location=' + data.location + '&totalseats=' + data.totalseats + '&upload=' + data.upload;
    return this.http.get(url);
  }
  getaddshowtimings(data: any): Observable<any> {
    const url = this.baseURL + 'addshowtimings?showtime=' + data.showtime + '&theatresid=' + data.theatresid + '&movieid=' + data.movieid;
    return this.http.get(url);
  }
  getshowtime(): Observable<any> {
    const url = this.baseURL + 'getshowtimes';
    return this.http.get(url);
  }
  getmovielist(): Observable<any> {
    const url = this.baseURL + 'getmovie';
    return this.http.get(url);
  }
  getbookedtickets(): Observable<any> {
    const url = this.baseURL + 'getmovie';
    return this.http.get(url);
  }
  gettheatreslist(): Observable<any> {
    const url = this.baseURL + 'gettheatres';
    return this.http.get(url);
  }
  booktickets(data: any): Observable<any> {
    const url = this.baseURL + 'booktickets?showid=' + data.showid + '&noofseats=' + data.noofseats+'&amount='+data.amount;
    return this.http.get(url);
  }
  viewTickets(): Observable<any> {
    const url = this.baseURL + 'viewTicket';
    return this.http.get(url);
  }
  validators(): Observable<any> {
    const url = this.baseURL + 'validate';
    return this.http.get(url);
  }
  deleteShowTimes(): Observable<any> {
    const url = this.baseURL + 'deleteshowtimes';
    return this.http.get(url);
  }
  cancelshows(data: any): Observable<any> {
    const url = this.baseURL + "cancelshows?showid="+data.showid;
    return this.http.get(url);
  }
}
