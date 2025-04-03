import { AuthService } from '@/services/auth.service';
import { Component, inject } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
    auth = inject(AuthService);

    credentials = {
        username: '',
        password: ''
    }


    onSubmit() {
        // Call the login function from AuthService here
        this.auth.login(this.credentials);
        
        // this.authService.login(this.credentials);   
    }

}
