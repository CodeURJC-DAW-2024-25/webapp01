import { Component } from '@angular/core';

type RegisterErrors = {
  username?: string;
  email?: string;
  password?: string;
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  // 
  errors: RegisterErrors = {}
}
