import { AuthService, AuthState } from "@/services/auth.service";
import { GlobalUser } from "@/types/User";
import { getDefaultAvatar } from "@/utils/defaultImage";
import { Component, inject, OnInit } from "@angular/core";

@Component({
	selector: "app-navbar",
	templateUrl: "./navbar.component.html",
	styleUrls: ["./navbar.component.css"]
})
export class NavbarComponent implements OnInit {
	isAuthenticated: boolean = false;
	isLoading: boolean = true;
	avatar: string = getDefaultAvatar();

	private authService = inject(AuthService);

	getDefaultAvatar(event: Event): void {
		const target = event.target as HTMLImageElement;
		target.src = getDefaultAvatar();
		target.alt = "Default Avatar";
	}

	ngOnInit(): void {
		this.authService.authState$.subscribe((authState: AuthState) => {
			this.isLoading = authState.isLoading;
			this.isAuthenticated = !!authState.user?.isAuthenticated;
			this.avatar = authState.user?.avatar || getDefaultAvatar();
		});
	}
} 
