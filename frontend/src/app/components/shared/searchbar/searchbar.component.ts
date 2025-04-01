import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
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
  @Input() searchQuery: string = ''; // Input for two-way binding
  @Output() searchQueryChange = new EventEmitter<string>(); // Output for two-way binding

  onSearch(): void {  
    this.searchQueryChange.emit(this.searchQuery); // Emit the updated search query
  }
} 

