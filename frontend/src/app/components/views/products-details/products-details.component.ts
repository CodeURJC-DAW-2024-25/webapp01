import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../../services/products.service';
import { AuthService, AuthState } from '@/services/auth.service';
import { AddListPopupComponent } from '../../shared/add-to-list-popup/add-to-list-popup.component';
import { ShoppingListService } from '@/services/shoppingList.service';

@Component({
    selector: 'app-product-details',
    templateUrl: './products-details.component.html',
    styleUrls: ['./products-details.component.css'],
})
export class ProductDetailsComponent implements OnInit {
    product: any;
    relatedProducts: any[] = [];
    isComparisonVisible: boolean = false;
    isAuthenticated: boolean = false;
    isLoading: boolean = true;
    showPopup: boolean = false;
    shoppingLists: any[] = [];
    productId: any;
    constructor(
        private route: ActivatedRoute,
        private productService: ProductService,
        private authService: AuthService,
        private shoppingListService: ShoppingListService
    ) {}

    ngOnInit(): void {
        this.authService.authState$.subscribe((authState: AuthState) => {
            this.isLoading = authState.isLoading;
            this.isAuthenticated = !!authState.user?.isAuthenticated;
            this.loadShoppingLists(authState.user?.user?.id ?? 0);
        });
        //Suscribe to updates in the route parameters
        this.route.paramMap.subscribe((params) => {
            this.productId = params.get('id');
            if (this.productId) {
                this.loadProductDetails(this.productId);
            }
        });
    }

    loadShoppingLists(id: number): void {
        this.shoppingListService.getUserLists(id).subscribe({
            next: (response) => {
                this.shoppingLists = response.data.page || [];
            },
            error: (error) => {
                console.error('Error al cargar las listas:', error);
            },
        });
    }
    loadProductDetails(productId: string): void {
        this.productService.getProductById(productId).subscribe({
            next: (response) => {
                this.product = response.data;
                this.productService.getRelatedProducts(productId).subscribe({
                    next: (relatedResponse) => {
                        this.relatedProducts = relatedResponse.data;
                    },
                    error: (error) => {
                        console.error(
                            'Error al cargar los productos relacionados:',
                            error
                        );
                    },
                });
            },
            error: (error) => {
                console.error(
                    'Error al cargar los detalles del producto:',
                    error
                );
            },
        });
    }

    addToList(): void {
        this.showPopup = true;
    }

    closePopup(): void {
        this.showPopup = false;
    }

    showComparison(): void {
        this.isComparisonVisible = true;
    }
}
