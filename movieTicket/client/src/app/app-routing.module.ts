import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { BookingComponent } from './user/booking/booking.component';
import { AddComponent } from './admin/add/add.component';
import { ShowsComponent } from './admin/shows/shows.component';

const routes: Routes = [
  {
    path: '',
    component: AppComponent
  },
  {
    path: 'user',
    component: BookingComponent
  },
  {
    path: 'admin',
    component: AddComponent
  },
  {
    path: 'showstiming',
    component: ShowsComponent
  }
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule],
})
export class AppRoutingModule { }


 