import { AuthService } from '@/services/auth.service';
import { Component, inject } from '@angular/core';

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
	auth = inject(AuthService);

	credentials = {
		email: '',
		username: '',
		password: ''
	}

	errors: RegisterErrors = {};

	onSubmit(): void {
		this.auth.register(this.credentials);
	}
}
