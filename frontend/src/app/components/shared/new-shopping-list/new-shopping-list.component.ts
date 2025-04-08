import { Component } from '@angular/core';
import { ShoppingListService } from '@/services/shoppingList.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-create-shopping-list',
    templateUrl: './new-shopping-list.component.html',
    styleUrls: ['./new-shopping-list.component.css'],
})
export class CreateShoppingListComponent {
    listName: string = '';
    listDescription: string = '';
    errorMessage: string | null = null;

    constructor(
        private shoppingListService: ShoppingListService,
        private router: Router
    ) {}

    createList(): void {
        if (this.listName.trim() && this.listDescription.trim()) {
            const request = {
                name: this.listName,
                description: this.listDescription,
            };

            this.shoppingListService.createShoppingList(request).subscribe({
                next: (response) => {
                    console.log('Lista creada con éxito:', response);
                    this.router.navigate(['/profile']);
                },
                error: (error) => {
                    console.error('Error al crear la lista:', error);
                    this.errorMessage =
                        'No se pudo crear la lista. Inténtalo de nuevo.';
                },
            });
        } else {
            this.errorMessage = 'Por favor, completa todos los campos.';
        }
    }
}
