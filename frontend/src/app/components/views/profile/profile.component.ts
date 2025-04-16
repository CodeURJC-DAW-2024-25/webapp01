import { Component, OnInit } from '@angular/core';
import { AuthService, AuthState } from '@/services/auth.service';
import { GlobalUser } from '@/types/User';
import { getDefaultAvatar } from '@/utils/defaultImage';
import { ShoppingListService } from '@/services/shoppingList.service';
@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
    user: GlobalUser | null = null;
    shoppingList: any[] = [];
    showCreateListModal: boolean = false;
    constructor(
        private authService: AuthService,
        private shoppingListService: ShoppingListService
    ) {}

    ngOnInit(): void {
        this.authService.authState$.subscribe((authState: AuthState) => {
            this.user = authState.user;
            if (this.user?.user?.id) {
                this.loadShoppingLists(this.user.user.id);
            }
        });
    }

    loadShoppingLists(userId: number): void {
        this.shoppingListService.getUserLists(userId).subscribe({
            next: (response) => {
                if (response.data.page && response.data.page.length > 0) {
                    this.shoppingList = response.data.page;
                } else {
                    this.shoppingList = [];
                }
            },
            error: (error) => {
                console.error('Error al cargar las listas:', error);
            },
        });
    }

    logout(): void {
        this.authService.logout();
    }

    setDefaultImage(event: Event): void {
        const target = event.target as HTMLImageElement;
        target.src = getDefaultAvatar();
        target.alt = 'Default Image';
    }

    // Function to chante the visibility
    toggleCreateListModal(): void {
        this.showCreateListModal = !this.showCreateListModal;
    }
    // Function to listen for the event emitted by the child component
    onListCreated(): void {
    if (this.user?.user?.id) {
        this.loadShoppingLists(this.user.user.id);
    }
}
}
