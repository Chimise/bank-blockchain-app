import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ApplicationRef } from '@angular/core';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http'; // Import HttpClientModule
import { AppRoutingModule } from './app.routes'; // Assuming you have an AppRoutingModule

@NgModule({
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule // Ensure you import AppRoutingModule
  ],
  providers: [], // Services that the app requires
})
export class AppModule { 
    ngDoBootstrap(appRef: ApplicationRef) {
        appRef.bootstrap(AppComponent); // Bootstrap the AppComponent manually
    }
}
