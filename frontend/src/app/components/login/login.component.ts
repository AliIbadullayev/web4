import {Component, OnInit,} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {delay, map} from "rxjs";
import {AuthService} from "../../auth.service";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form!: FormGroup;

  error: boolean = false;
  existing_error: boolean = false;
  message!:string;

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {
  }

  ngOnInit(): void {
    //Here I check that user is authorized or not
    this.authService.isLoggedIn$.subscribe(res=>{
     if ( res.valueOf()){
       this.router.navigate(['/main']);
     } else this.router.navigate(['/start/login'])
    });

    this.form = this.formBuilder.group({
      username:'',
      password:''
    });
  }

  onSubmit() {
    if (!this.form.value.username || !this.form.value.password) {
      this.existing_error = false;
      this.error = true;
      return;
    }
    this.authService.login(this.form)
      .subscribe(()=>{
        this.ngOnInit();
        this.error = false;
        this.existing_error = false;
        this.router.navigate(['/main']);
        return;
    }, () => {
        this.error = false;
        this.existing_error = true;
        return ;
      })
  }
}


