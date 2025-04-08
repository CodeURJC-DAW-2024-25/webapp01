import { AuthService, AuthState } from "@/services/auth.service";
import { Component, inject, OnInit } from "@angular/core";

@Component({
	selector: "app-hero",
	templateUrl: "./hero.component.html",
	styleUrl: "./hero.component.css"
})
export class HeroComponent implements OnInit {
	authService = inject(AuthService);

	private _isLoading = true;
	private _isAdmin = false;

	ngOnInit(): void {
		this.authService.authState$.subscribe((authState: AuthState) => {
			this._isLoading = authState.isLoading;
			this._isAdmin = authState.user?.isAdmin || false;
		});
	}

	get isAdmin(): boolean {
		return this._isAdmin;
	}

	set isAdmin(value: boolean) {
		this._isAdmin = value;
	}

	get isLoading(): boolean {
		return this._isLoading;
	}

	set isLoading(value: boolean) {
		this._isLoading = value;
	}
}
