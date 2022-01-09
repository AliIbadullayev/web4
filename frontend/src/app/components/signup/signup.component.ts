import {Component, OnInit} from '@angular/core';

import {FormBuilder, FormGroup} from "@angular/forms";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map} from "rxjs";


@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  form!: FormGroup;

  error: boolean = false;
  good: boolean = false;
  existed_user: boolean = false;


  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient
  ) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: '',
      password:''
    });
  }

  onSubmit() {
    if (!this.form.value.username || !this.form.value.password) {
      this.existed_user = false;
      this.error = true;
      return;
    }
    console.log(this.form.getRawValue());
    this.http.post('/rest/register', this.form.value )
      .subscribe(res=>{
        if (res != null){
          this.ngOnInit();
          this.good = true;
          this.error = false;
          this.existed_user = false;
          return;
        }
        this.good = false;
        this.error = false;
        this.existed_user = true;
        return;
      })
  }
}
