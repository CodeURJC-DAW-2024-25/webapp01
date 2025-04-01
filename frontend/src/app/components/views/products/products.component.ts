import { Component } from '@angular/core';
import { ProductService } from '../../../services/products/products.service'; // Import the ProductService

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent {
  searchQuery: string = ''; // Two-way binding property
  products: any[] = []; // Array to store the products

  constructor(private productService: ProductService) {}

  ngOnChanges(): void {
    // Trigger product search whenever the search query changes
    this.loadProducts();
  }

  loadProducts(): void {
    console.log('Loading products with search query:', this.searchQuery); // Debugging log
    if (this.searchQuery.trim() === '') {
      this.products = []; // Clear products if the search query is empty
      return;
    }

    this.productService.searchProducts(this.searchQuery).subscribe(
      (products) => {
        this.products = products; // Update the products array
        console.log('Products loaded:', this.products); // Debugging log
      },
      (error) => {
        console.error('Error fetching products:', error);
      }
    );
  }
}