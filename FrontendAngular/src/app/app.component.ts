import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';

import {} from '@angular/common/http';
import { HomeComponent } from './components/home/home.component';

@Component({
    selector: 'app-root',
    imports: [CommonModule, RouterOutlet, RouterOutlet],
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {

}
