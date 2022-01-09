import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "./auth.service";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
constructor(
  public authService: AuthService,
  private router: Router) {
}

  ngOnInit(): void {
    this.authService.isLoggedIn$.subscribe(res => {
      if (res.valueOf()) {
        this.router.navigate(['/main']);
      } else this.router.navigate(['/start/login'])
    });
  }
}
