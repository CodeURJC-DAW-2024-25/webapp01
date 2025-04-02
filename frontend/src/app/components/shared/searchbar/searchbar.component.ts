import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { UserDataService } from '@services/user-data.service';

@Component({
    selector: 'app-searchbar',
    templateUrl: './searchbar.component.html',
    styleUrls: ['./searchbar.component.css'],
})
export class SearchbarComponent {
    userData: UserDataService = inject(UserDataService);

    @Input() searchQuery: string = '';
    @Output() searchQueryChange = new EventEmitter<string>();

    private router = inject(Router);
    private route = inject(ActivatedRoute);

    onSearch(): void {
        this.searchQueryChange.emit(this.searchQuery);
        //Check if the user is in the products page or not
        if (this.router.url.startsWith('/products')) {
            this.router.navigate([], {
                relativeTo: this.route,
                queryParams: { search: this.searchQuery },
                queryParamsHandling: 'merge',
            });
        } else {
            this.router.navigate(['/products'], {
                queryParams: { search: this.searchQuery },
            });
        }
    }
}
