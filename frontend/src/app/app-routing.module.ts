import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {SignupComponent} from "./components/signup/signup.component";
import {MainComponent} from "./components/main/main.component";
import {StartComponent} from "./components/start/start.component";

const routes: Routes = [
  {
    path: 'start', component: StartComponent, children: [
      {
        path: 'login', component: LoginComponent,
      },
      {
        path: 'register', component: SignupComponent,
      }
    ]
  },
  {
    path: 'main', component: MainComponent,
  }, {
    path: '',
    redirectTo: '/start/login',
    pathMatch: 'full'
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
