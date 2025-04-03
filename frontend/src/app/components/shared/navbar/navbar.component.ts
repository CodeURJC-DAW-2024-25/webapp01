import { AuthService } from "@/services/auth.service";
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
	avatar: string = getDefaultAvatar();

	private authService = inject(AuthService);

	getDefaultAvatar(event: Event): void {
		const target = event.target as HTMLImageElement;
		target.src = getDefaultAvatar();
		target.alt = "Default Avatar";
	}

	ngOnInit(): void {
		this.authService.globalUser$.subscribe((user: GlobalUser | null) => {
			this.isAuthenticated = !!user?.isAuthenticated;
			this.avatar = user?.avatar || getDefaultAvatar();
		});
	}
} 
