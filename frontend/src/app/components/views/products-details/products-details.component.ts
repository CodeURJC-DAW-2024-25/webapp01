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
        this.isComparisonVisible = true;
    }
}
// document.addEventListener("DOMContentLoaded", function () {
//     const compareBtn = document.getElementById("compareBtn")
//     const compareContainer = document.getElementById("compareContainer")

//     compareBtn.addEventListener("click", function (e) {
//         e.preventDefault()

//         const productName = document.getElementById("productTitle")?.textContent.trim()

//         compareContainer.innerHTML = "<p>Cargando resultados...</p>"
//         compareContainer.style.display = "block"
//         compareBtn.innerHTML = "<i class='bi bi-arrow-repeat spin'></i>"

//         fetchData(`/products?keywords=${keywordsUnparsed}&limit=1000&page=0`, "GET", {
//             cacheData: false
//         })
//         .then(res => {
//             let products = res.data.page
//             const mainProduct = { productName, keywords, brand, }
//             const bestBySupermarket = comparationAlgorithm(mainProduct, products)

//             if (Object.keys(bestBySupermarket).length === 0) {
//                 compareContainer.innerHTML = "<p>Lo sentimos, no se encontraron productos similares.</p>"
//                 compareBtn.innerHTML = "<i class='bi bi-x-lg'></i>"
//             } else {
//                 const encodedProducts = Object.values(bestBySupermarket).map(p => `${p.supermarket_name}@${p.display_name}@${p.price.total}@${p.product_url}`).join("_")
//                 fetch("/get-compare-table?products=" + encodedProducts)
//                     .then(response => response.text())
//                     .then(html => {
//                         compareContainer.innerHTML = html
//                         compareBtn.innerHTML = "<i class='bi bi-check-lg'></i>"
//                         highlightBestAndWorstPrices()

//                         // Add link to product
//                         document.querySelectorAll("[data-product-url]").forEach(a => a.addEventListener("click", () => window.open(a.dataset.productUrl, "_blank")))
//                     })
//             }
//         })
//     })
// })
