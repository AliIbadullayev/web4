import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  constructor(
    public authService: AuthService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    // Check that user authorized or not
    this.authService.isLoggedIn$.subscribe(res=>{
      if ( res.valueOf()){
        this.router.navigate(['/main']);
      } else this.router.navigate(['/start/login'])
    });
  }

}
