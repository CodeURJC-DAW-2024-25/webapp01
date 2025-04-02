import { Component, OnInit } from '@angular/core';
import { ProductService } from '@services/products.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  products: any[] = [];
  supermarkets: { id: string; name: string }[] = [];
  currentPage: number = 0;
  totalPages: number = Infinity;
  isEnd: boolean = false;
  searchQuery: string = '';
  filters: { supermarket?: string; minPrice?: string; maxPrice?: string } = {};

  constructor(private productService: ProductService, private route: ActivatedRoute) {}

  ngOnInit(): void {

    this.supermarkets = [
      { id: 'dia', name: 'Día' },
      { id: 'mercadona', name: 'Mercadona' },
      { id: 'bm', name: 'BM' },
      { id: 'consum', name: 'Consum' },
      { id: 'elcorteingles', name: 'ElCorteIngles' }
    ];
    this.route.queryParams.subscribe((params) => {
      this.searchQuery = params['search'] || '';
      this.filters.supermarket = params['supermarket'] || '';
      this.filters.minPrice = params['minPrice'] || '';
      this.filters.maxPrice = params['maxPrice'] || '';
      this.loadProducts();
    });

  }

  loadProducts(page: number = 0): void {
    if (page < 0 || (this.isEnd && page > this.currentPage)) return;

    this.productService.loadProducts(page, this.searchQuery, this.filters).subscribe(
      (response) => {
        const data = response.data;
        this.products = data.page;
        this.currentPage = data.current_page;
        this.totalPages = data.total_pages < 1 ? 1 : data.total_pages + 1;
        this.isEnd = this.currentPage >= this.totalPages - 1;
      },
      (error) => {
        console.error('Error loading products:', error);
      }
    );
  }

  applyFilters(): void {
    this.currentPage = 0;
    this.isEnd = false;
    this.loadProducts();
  }
}