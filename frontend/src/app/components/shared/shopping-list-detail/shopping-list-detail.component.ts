import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ShoppingListService } from '@/services/shoppingList.service';

@Component({
    selector: 'app-shopping-list-details',
    templateUrl: './shopping-list-detail.component.html',
    styleUrls: ['./shopping-list-detail.component.css'],
})
export class ShoppingListDetailsComponent implements OnInit {
    shoppingList: any = null;
    errorMessage: string | null = null;

    constructor(
        private route: ActivatedRoute,
        private shoppingListService: ShoppingListService,
        private router: Router
    ) {}

    ngOnInit(): void {
        const listId = this.route.snapshot.paramMap.get('id');
        if (listId) {
            this.loadShoppingListDetails(+listId);
        }
    }

    loadShoppingListDetails(listId: number): void {
        this.shoppingListService.getShoppingListById(listId).subscribe({
            next: (response) => {
                this.shoppingList = response;              
            },
            error: (error) => {
                console.error(
                    'Error al cargar los detalles de la lista:',
                    error
                );
                this.errorMessage = 'No se pudo cargar la lista de compras.';
            },
        });
    }
    deleteProduct(productId: string): void {
        this.shoppingListService
            .removeProductFromList(this.shoppingList.id, productId)
            .subscribe({
                next: () => {                  
                        this.shoppingList.products =
                        this.shoppingList.products.filter(
                            (product: any) => product.product_id !== productId
                        );
                },
                error: (error) => {
                    console.error('Error al eliminar el producto:', error);
                },
            });
    }

    deleteShoppingList(): void {
        if (this.shoppingList?.id) {
            this.shoppingListService
                .deleteShoppingList(this.shoppingList.id)
                .subscribe({
                    next: () => {
                        this.router.navigate(['/profile']); 
                    },
                    error: (error) => {
                        console.error('Error al eliminar la lista:', error);
                        this.errorMessage = 'No se pudo eliminar la lista.';
                    },
                });
        }
    }
}
