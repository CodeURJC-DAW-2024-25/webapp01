import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../../services/products.service';
import { AuthService, AuthState } from '@/services/auth.service';

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
      
    
      
    
    constructor(
        private route: ActivatedRoute,
        private productService: ProductService,
        private authService: AuthService
    ) {}

    ngOnInit(): void {
        this.authService.authState$.subscribe((authState: AuthState) => {
                    this.isLoading = authState.isLoading;
                    this.isAuthenticated = !!authState.user?.isAuthenticated;
        });
        //Suscribe to updates in the route parameters
        this.route.paramMap.subscribe((params) => {
            const productId = params.get('id');
            if (productId) {
                this.loadProductDetails(productId);
            }
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
        
    }

    showComparison(): void {
        this.isComparisonVisible = true;
    }
}
