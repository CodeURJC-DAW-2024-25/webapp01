import { AuthService } from "@/services/auth.service";
import { GlobalUser } from "@/types/User";
import { Component, inject, OnInit } from "@angular/core";
import { UserDataService } from "@services/user-data.service";

@Component({
	selector: "app-cta",
	templateUrl: "./cta.component.html",
	styleUrl: "./cta.component.css"
})
export class CtaComponent implements OnInit {
	isAuthenticated = false;

	private authService = inject(AuthService);

	ngOnInit(): void {
		this.authService.globalUser$.subscribe((user: GlobalUser | null) => {
			this.isAuthenticated = !!user?.isAuthenticated;
		});
	}
}
