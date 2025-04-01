import { Component } from '@angular/core';
import { ProductService } from '../../../services/products/products.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent {
  private _searchQuery: string = ''; // Propiedad privada para el getter/setter
  products: any[] = []; // Array para almacenar los productos

  constructor(private productService: ProductService) {}

  get searchQuery(): string {
    return this._searchQuery;
  }

  set searchQuery(value: string) {
    this._searchQuery = value;
    this.loadProducts(); // Llama al método asíncrono cuando cambia el valor
  }

  loadProducts(): void {
    console.log('Loading products with search query:', this.searchQuery); // Debugging log
    if (this.searchQuery.trim() === '') {
      this.products = []; // Limpia los productos si la búsqueda está vacía
      return;
    }

    this.productService.searchProducts(this.searchQuery).subscribe(
      (products) => {
        this.products = products; // Actualiza el array de productos
        console.log('Products loaded:', this.products); // Debugging log
      },
      (error) => {
        console.error('Error fetching products:', error);
      }
    );
  }
}