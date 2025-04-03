import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
    providedIn: 'root',
})
export class ProductService {
    private readonly PRODUCTS_SIZE = 24;
    private apiUrl = `${environment.baseApiUrl}/v1/products`;
    constructor(private http: HttpClient) {}

    loadProducts(
        page: number = 0,
        searchQuery: string = '',
        filters: {
            supermarket?: string;
            minPrice?: string;
            maxPrice?: string;
        } = {}
    ): Observable<any> {
        let endpoint = `${this.apiUrl}?page=${page}&limit=${
            this.PRODUCTS_SIZE
        }&search=${encodeURIComponent(searchQuery)}`;
        if (filters.supermarket)
            endpoint += `&supermarket=${filters.supermarket}`;
        if (filters.minPrice)
            endpoint += `&minPrice=${encodeURIComponent(filters.minPrice)}`;
        if (filters.maxPrice)
            endpoint += `&maxPrice=${encodeURIComponent(filters.maxPrice)}`;

        return this.http.get(endpoint);
    }

    getProductById(productId: string): Observable<any> {
        const endpoint = `${this.apiUrl}/${encodeURIComponent(productId)}`;
        return this.http.get(endpoint);
    }

    getRelatedProducts(productId: string): Observable<any> {
        const endpoint = `${this.apiUrl}/${encodeURIComponent(productId)}/suggested`;

        return this.http.get(endpoint);
    }

    compareProducts(productId: string): Observable<any> {
        const endpoint = `${this.apiUrl}/${encodeURIComponent(productId)}/compare`;
        console.log('Comparando productos en:', endpoint);
        return this.http.get(endpoint);
    }
}
