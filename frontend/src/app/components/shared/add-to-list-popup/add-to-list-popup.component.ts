import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ShoppingListService } from '@/services/shoppingList.service';

@Component({
    selector: 'app-add-list-popup',
    templateUrl: './add-to-list-popup.component.html',
    styleUrls: ['./add-to-list-popup.component.css'],
})
export class AddListPopupComponent implements OnInit {
    @Input() lists: any[] = [];
    @Input() productId!: string;
    @Output() close = new EventEmitter<void>();
    @Output() listSelected = new EventEmitter<number>();
    popupTitle: string = 'Añadir lista';
    errorMessage: string | null = null;

    constructor(private shoppingListService: ShoppingListService) {}

    ngOnInit(): void {}

    selectList(listId: number): void {
        if (this.productId) {
            this.shoppingListService
                .addProductToList(listId, this.productId)
                .subscribe({
                    next: () => {
                        this.listSelected.emit(listId);
                        this.closePopup();
                    },
                    error: (error) => {
                        console.error(
                            'Error al añadir el producto a la lista:',
                            error
                        );
                        this.errorMessage =
                            'No se pudo añadir el producto a la lista.';
                    },
                });
        } else {
            console.error('No se proporcionó un ID de producto.');
            this.errorMessage =
                'No se pudo añadir el producto porque no se proporcionó un ID.';
        }
    }

    closePopup(): void {
        this.close.emit();
    }
}
