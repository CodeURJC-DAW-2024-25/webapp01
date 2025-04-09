import { AuthService, AuthState } from "@/services/auth.service";
import { Component, inject, OnInit } from "@angular/core";

@Component({
	selector: "app-cta",
	templateUrl: "./cta.component.html",
	styleUrl: "./cta.component.css"
})
export class CtaComponent implements OnInit {
	isAuthenticated = false;

	private authService = inject(AuthService);

	ngOnInit(): void {
		this.authService.authState$.subscribe((authState: AuthState) => {
			this.isAuthenticated = !!authState.user?.isAuthenticated;
		});
	}
}
