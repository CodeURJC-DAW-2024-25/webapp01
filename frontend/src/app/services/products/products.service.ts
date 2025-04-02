  import { Injectable } from '@angular/core';
  import { HttpClient } from '@angular/common/http';
  import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
  
  @Injectable({
    providedIn: 'root'
  })
  export class ProductService {
    private readonly PRODUCTS_SIZE = 24;
    private apiUrl = `${environment.baseApiUrl}/products`;
    constructor(private http: HttpClient) {}
  
    loadProducts(
      page: number = 0,
      searchQuery: string = '',
      filters: { supermarket?: string; minPrice?: string; maxPrice?: string } = {}
    ): Observable<any> {
      
      let endpoint = `${this.apiUrl}?page=${page}&limit=${this.PRODUCTS_SIZE}&search=${encodeURIComponent(searchQuery)}`;
      if (filters.supermarket) endpoint += `&supermarket=${encodeURIComponent(filters.supermarket)}`;
      if (filters.minPrice) endpoint += `&minPrice=${encodeURIComponent(filters.minPrice)}`;
      if (filters.maxPrice) endpoint += `&maxPrice=${encodeURIComponent(filters.maxPrice)}`;
  
      return this.http.get(endpoint);
    }
  }