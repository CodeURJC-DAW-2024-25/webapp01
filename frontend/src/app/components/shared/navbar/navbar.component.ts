import { Component, inject } from '@angular/core';
import { UserDataService } from '../../../services/templates/user-data.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css' ]
})
export class NavbarComponent {
  // --- Dependency Injection ---
  userDataService: UserDataService = inject(UserDataService);

} 
