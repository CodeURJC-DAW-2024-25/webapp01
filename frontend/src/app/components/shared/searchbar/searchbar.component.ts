import { Component, inject } from '@angular/core';
import { UserDataService } from '../../../services/templates/user-data.service';

@Component({
  selector: 'app-searchbar',
  templateUrl: './searchbar.component.html',
  styleUrl: './searchbar.component.css'
})
export class SearchbarComponent {
  // --- Dependencies ---
  userData: UserDataService = inject(UserDataService);

  // --- Properties ---
  searchQuery: string = '';
}
