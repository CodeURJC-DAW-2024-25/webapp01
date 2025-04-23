import { AuthService, AuthState } from "@/services/auth.service";
import { Component, inject, OnInit } from "@angular/core";
import { environment } from "@environments/environment";

@Component({
	selector: "app-cta",
	templateUrl: "./cta.component.html",
	styleUrl: "./cta.component.css"
})
export class CtaComponent implements OnInit {
	private authService = inject(AuthService);

	isAuthenticated = false;
	image: string = `${environment.production ? "/new" : ""}/assets/images/cta-image.jpeg`;

	ngOnInit(): void {
		this.authService.authState$.subscribe((authState: AuthState) => {
			this.isAuthenticated = !!authState.user?.isAuthenticated;
		});
	}
}
