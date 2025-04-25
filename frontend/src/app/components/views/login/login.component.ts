import { AuthService } from '@/services/auth.service';
import { Component, inject } from '@angular/core';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
    auth = inject(AuthService);

    launchPopup: boolean = false;
    popupHeader: string = '';
    popupContent: string = '';
    credentials = {
        username: '',
        password: ''
    }


    onSubmit(): void {
        // Call the login function from AuthService here
        this.auth.login(this.credentials).subscribe({
            error: (error) => {
                this.launchPopup = true;
                this.popupHeader = 'Fallo de inicio de sesión';
                this.popupContent = 'Error en las credenciales';
            }
        });
    }

    onClosePopup(): void {
        this.launchPopup = false;
      }
}
