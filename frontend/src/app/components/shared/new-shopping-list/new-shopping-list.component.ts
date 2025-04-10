import { Component, EventEmitter, Output } from '@angular/core';
import { ShoppingListService } from '@/services/shoppingList.service';

@Component({
    selector: 'app-create-shopping-list',
    templateUrl: './new-shopping-list.component.html',
    styleUrls: ['./new-shopping-list.component.css'],
})
export class CreateShoppingListComponent {
    @Output() close = new EventEmitter<void>();
    @Output() listCreated = new EventEmitter<void>();
    listName: string = '';
    listDescription: string = '';
    errorMessage: string | null = null;

    constructor(private shoppingListService: ShoppingListService) {}

    createList(): void {
        if (this.listName.trim() && this.listDescription.trim()) {
            const request = {
                name: this.listName,
                description: this.listDescription,
            };  

            this.shoppingListService.createShoppingList(request).subscribe({
                next: () => {
                    console.log('Lista creada con éxito');
                    this.listCreated.emit(); // Emit the event to update the lists in profile page 
                    this.close.emit();
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

    closeModal(): void {
        this.close.emit(); 
    }
}