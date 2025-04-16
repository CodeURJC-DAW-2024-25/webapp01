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
    private route: ActivatedRoute = inject(ActivatedRoute);
    private productService: ProductService = inject(ProductService);
    private authService: AuthService = inject(AuthService);
    private shoppingListService: ShoppingListService = inject(ShoppingListService);

    product: any;
    relatedProducts: any[] = [];
    isComparisonVisible: boolean = false;
    isAuthenticated: boolean = false;
    isLoading: boolean = true;
    isLoadingProduct: boolean = true;
    showPopup: boolean = false;
    shoppingLists: any[] = [];
    productId: any;

    ngOnInit(): void {
        this.productId = this.route.snapshot.params['id'];
        this.loadProductDetails(this.productId);
        
        this.authService.authState$.subscribe((authState: AuthState) => {
            this.isLoading = authState.isLoading;
            this.isAuthenticated = !!authState.user?.isAuthenticated;
            if (this.isAuthenticated) {
                this.loadShoppingLists(authState.user?.user?.id ?? 0);
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
                this.isLoadingProduct = false;
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
