import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { ShoppingListService } from '@/services/shoppingList.service';
import { ShoppingList } from '@/types/ShoppingList';

@Component({
    selector: 'app-add-product-modal',
    templateUrl: './add-product-modal.component.html',
    styleUrls: ['./add-product-modal.component.css'],
})
export class AddProductModal {
    private shoppingListService: ShoppingListService = inject(ShoppingListService);
    @Input() lists: ShoppingList[] | null = [];
    @Input() productId!: string;
    @Output() close = new EventEmitter<void>();

    productsAdded: number[] = [];
    errorMessage: string | null = null;

    selectList(listId: number): void {
        if (this.productId) {
            this.shoppingListService
                .addProductToList(listId, this.productId)
                .subscribe({
                    next: () => {
                        this.productsAdded.push(listId);
                    },
                    error: (error) => {
                        console.error(error);
                        this.errorMessage ='No se pudo añadir el producto a la lista.';
                    },
                });
        } else {
            console.error('No product ID provided.');
            this.errorMessage ='No se pudo añadir el producto porque no se proporcionó un ID.';
        }
    }

    closePopup(): void {
        this.close.emit();
    }
}
