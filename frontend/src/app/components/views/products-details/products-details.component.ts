import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../../services/products.service';

@Component({
    selector: 'app-product-details',
    templateUrl: './products-details.component.html',
    styleUrls: ['./products-details.component.css'],
})
export class ProductDetailsComponent implements OnInit {
    product: any;
    relatedProducts: any[] = [];
    isComparisonVisible: boolean = false; 
    constructor(
        private route: ActivatedRoute,
        private productService: ProductService
    ) {}

    ngOnInit(): void {
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
                        console.log(
                            'Productos relacionados asignados:',
                            this.relatedProducts
                        );
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


    showComparison(): void {
    console.log('Botón Comparar presionado');
    this.isComparisonVisible = true;
}
}
