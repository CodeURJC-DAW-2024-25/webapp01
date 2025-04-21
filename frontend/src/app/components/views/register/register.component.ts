import { AuthService } from '@/services/auth.service';
import { Component, inject } from '@angular/core';
import { isValidUser } from '@/utils/validationUtils';
import { RegisterUser } from "@/types/User";

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

	errors: RegisterUser = {
		email: '',
		username: '',
		password: ''
	}

	errorMessage: string = '';

	onSubmit(): void {
		this.errors = isValidUser(this.credentials);
		if (this.errors.email === '' && this.errors.username === '' && this.errors.password === '') {
			this.auth.register(this.credentials).subscribe({
				error: (err) => {
					console.error(err);
					this.errorMessage = err.error.error.message;
				}
			});
		}
	}
}
