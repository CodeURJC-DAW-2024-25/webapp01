import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ShoppingListService } from '@/services/shoppingList.service';
import { ShoppingListDetails } from '@/types/ShoppingList';
import { Product } from '@/types/Product';

@Component({
    selector: 'app-shopping-list-details',
    templateUrl: './shopping-list-detail.component.html',
    styleUrls: ['./shopping-list-detail.component.css'],
})
export class ShoppingListDetailsComponent implements OnInit {
    shoppingList: ShoppingListDetails = {
        id: -1,
        name: '',
        description: '',
        numberOfProducts: 0,
        products: [],
    };
    errorMessage: string | null = null;

    constructor(
        private route: ActivatedRoute,
        private shoppingListService: ShoppingListService,
        private router: Router
    ) {}

    ngOnInit(): void {
        const listId = this.route.snapshot.paramMap.get('id');
        if (listId) this.loadShoppingListDetails(+listId);
    }

    loadShoppingListDetails(listId: number): void {
        this.shoppingListService.getShoppingListById(listId).subscribe({
            next: (response) => {
                console.log(response);
                this.shoppingList = response.data;
            },
            error: (error) => {
                console.error(error);
                this.errorMessage = 'No se pudo cargar la lista de compras.';
            },
        });
    }
    deleteProduct(productId: string): void {
        this.shoppingListService
            .removeProductFromList(this.shoppingList?.id || -1, productId)
            .subscribe({
                next: () => {                  
                        this.shoppingList.products = this.shoppingList.products.filter(
                            (product: Product) => product.product_id !== productId
                        );
                        this.shoppingList.numberOfProducts = this.shoppingList.products.length;
                        this.errorMessage = null;
                },
                error: (error) => {
                    console.error(error);
                    this.errorMessage = 'Error al eliminar el producto.';
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
                        console.error(error);
                        this.errorMessage = 'No se pudo eliminar la lista.';
                    },
                });
        }
    }
}
