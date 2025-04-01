import { Component, inject } from "@angular/core";
import { UserDataService } from "@services/user-data.service";

@Component({
  selector: "app-cta",
  templateUrl: "./cta.component.html",
  styleUrl: "./cta.component.css"
})
export class CtaComponent {
  userData: UserDataService = inject(UserDataService);

}
