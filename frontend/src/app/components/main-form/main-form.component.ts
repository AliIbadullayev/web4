import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {FormBuilder, FormGroup} from "@angular/forms";

interface POINTS {
  x: Number;
  y: Number;
  radius: Number;
  result: Boolean;
}

@Component({
  selector: 'app-main-form',
  templateUrl: './main-form.component.html',
  styleUrls: ['./main-form.component.css']
})
export class MainFormComponent implements OnInit {
  @ViewChild('canvas', { static: true }) canvas!: ElementRef<HTMLCanvasElement>;

  @ViewChild('rx', { static: true }) rx!: ElementRef;
  @ViewChild('minus_rx', { static: true }) minus_rx!: ElementRef;
  @ViewChild('minus_halfrx', { static: true }) minus_halfrx!: ElementRef;
  @ViewChild('halfrx', { static: true }) halfrx!: ElementRef;
  @ViewChild('ry', { static: true }) ry!: ElementRef;
  @ViewChild('minus_ry', { static: true }) minus_ry!: ElementRef;
  @ViewChild('minus_halfry', { static: true }) minus_halfry!: ElementRef;
  @ViewChild('halfry', { static: true }) halfry!: ElementRef;

  ctx!: CanvasRenderingContext2D ;
  form!: FormGroup;
  Points!: POINTS[];
  result!: POINTS;
  json!: object;
  choose_radius_error!: boolean;
  server_error!: boolean;
  validate_error_x!: boolean;
  validate_error_y!: boolean;


  xss: any[] = [
    {key: 'x_minus_3', value: -3},
    {key: 'x_minus_2', value: -2},
    {key: 'x_minus_1', value: -1},
    {key: 'x_0', value: 0},
    {key: 'x_1', value: 1},
    {key: 'x_2', value: 2},
    {key: 'x_3', value: 3},
    {key: 'x_4', value: 4},
    {key: 'x_5', value: 5}
  ];
  radiuss: any[] = [
    {key: 'radius_1', value: 1},
    {key: 'radius_2', value: 2},
    {key: 'radius_3', value: 3},
    {key: 'radius_4', value: 4},
    {key: 'radius_5', value: 5}
  ];

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient,
  ) {
  }


  ngOnInit(): void {
    this.form = this.formBuilder.group({
      x: null,
      y: null,
      radius: null
    });

    this.choose_radius_error = false;

    this.rx.nativeElement.innerHTML= 'R';
    this.minus_rx.nativeElement.innerHTML = "-R";
    this.halfrx.nativeElement.innerHTML = 'R/2' ;
    this.minus_halfrx.nativeElement.innerHTML = "-R/2" ;

    this.ry.nativeElement.innerHTML = 'R';
    this.minus_ry.nativeElement.innerHTML = "-R";
    this.halfry.nativeElement.innerHTML = 'R/2' ;
    this.minus_halfry.nativeElement.innerHTML = "-R/2" ;

    this.getPoints();
  }

  ngOnInitWithoutGetPoints(): void {
    this.form = this.formBuilder.group({
      x: null,
      y: null,
      radius: null
    });

    this.choose_radius_error = false;
    this.validate_error_x = false;
    this.validate_error_y = false;
    this.server_error = false;

    this.rx.nativeElement.innerHTML= 'R';
    this.minus_rx.nativeElement.innerHTML = "-R";
    this.halfrx.nativeElement.innerHTML = 'R/2' ;
    this.minus_halfrx.nativeElement.innerHTML = "-R/2" ;

    this.ry.nativeElement.innerHTML = 'R';
    this.minus_ry.nativeElement.innerHTML = "-R";
    this.halfry.nativeElement.innerHTML = 'R/2' ;
    this.minus_halfry.nativeElement.innerHTML = "-R/2" ;

  }

  getPoints() {
    // @ts-ignore
    let token = JSON.parse(localStorage.getItem("currentUser"));
    let headers= new HttpHeaders()
      .set('Authorization', "Bearer " + token )
      .set('Content-Type', "application/json");

    this.http.get('/rest/getpoints', {headers: headers})
      .subscribe(res=>{
        if (res != null){

          // @ts-ignore
          localStorage.setItem('tempPoints', JSON.stringify(res));
          // @ts-ignore
          this.Points = res;

          //Here I draw all points on open of page
          this.drawPoints();
          return;
        }
        return;
      })
  }

  drawPoints() {
    // @ts-ignore
    this.ctx = this.canvas.nativeElement.getContext('2d');
    this.ctx.beginPath();
    for(let result of this.Points){
      // @ts-ignore
      this.ctx.fillStyle = !result.result ? "red": "green";
      this.ctx.beginPath();
      let x  = result.x.valueOf();
      let y = result.y.valueOf();
      let rad = result.radius.valueOf();
      this.ctx.arc(
        x / rad * 68 + 220 / 2,
        - y / rad * 68 + 220 / 2,
        3, 0, 2 * Math.PI);
      this.ctx.fill();
    }
  }

  mouseEnter() {
    if (this.form.value.radius == null){
      this.choose_radius_error = true;
    } else {
      this.choose_radius_error = false;
      this.validate_error_y = false;
      this.validate_error_x = false;
      this.rx.nativeElement.innerHTML= this.form.value.radius;
      this.minus_rx.nativeElement.innerHTML = -this.form.value.radius;
      this.halfrx.nativeElement.innerHTML =  this.form.value.radius/2
      this.minus_halfrx.nativeElement.innerHTML =  -this.form.value.radius/2;

      this.ry.nativeElement.innerHTML = this.form.value.radius;
      this.minus_ry.nativeElement.innerHTML = -this.form.value.radius;
      this.halfry.nativeElement.innerHTML =  this.form.value.radius/2;
      this.minus_halfry.nativeElement.innerHTML =  -this.form.value.radius/2;
    }
  }

  mouseClick(evt: MouseEvent){
    if(this.form.value.radius !== null) {
      let xcheck: number = Number(((evt.offsetX - 110) / 33 * this.form.value.radius / 2).toFixed(2));
      let ycheck: number = Number((-(evt.offsetY - 110) / 33 * this.form.value.radius / 2).toFixed(2));

      this.form.value.x = this.validateX(xcheck);
      this.form.value.y = this.validateY(ycheck);
      if (this.form.value.x != null && this.form.value.y != null) {
        this.validate_error_x = false;
        this.validate_error_y = false;
        this.onSubmit();
      }
      else {
        if (!this.form.value.x){
          this.validate_error_x = true;
        } if (!this.form.value.y) {
          this.validate_error_y = true;
        }
        return;
      }
    }
  }

  validateX(xcheck: number): number | null {
    if (xcheck == -0){
      return 0;
    } else if(xcheck<=5 && xcheck>=-3 ) {return xcheck;}
    else {
      return null;
    }
  }

  validateY(ycheck: number): number | null {
    if (ycheck == -0){
      return 0;
    } else if(ycheck<=5 && ycheck>=-3 ) {return ycheck;}
    else {
      return null;
    }
  }


  onSubmit() {
    if (this.form.value.x == null || this.form.value.y == null || this.form.value.radius == null) {
      this.validate_error_x = this.form.value.x == null;
      this.validate_error_y = this.form.value.y == null;
      this.choose_radius_error = this.form.value.radius == null;
      return;
    }
    else {
      // @ts-ignore
      const token = JSON.parse(localStorage.getItem("currentUser"));
      const headers= new HttpHeaders()
        .set('Authorization', "Bearer " + token );

      this.http.post('/rest/addpoint', this.form.value, {headers: headers})
        .subscribe(res=>{
            this.server_error = false;
            this.ngOnInitWithoutGetPoints();
            // @ts-ignore
            localStorage.setItem('tempPoints', JSON.stringify(res));
            // @ts-ignore
            this.Points = res;
            this.drawPoints();
            return;
        },
          () => {
            this.server_error = true;
            return;
          })
    }
  }
}
