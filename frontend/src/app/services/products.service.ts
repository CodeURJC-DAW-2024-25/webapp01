import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { Product } from '@/types/Product';
import { Response } from '@/types/common/Response';

@Injectable({
    providedIn: 'root',
})
export class ProductService {
    private readonly PRODUCTS_SIZE = 24;
    private readonly CACHE_TIMEOUT = 3600000;   // -> 1 hour
    private apiUrl = `${environment.baseApiUrl}/v1/products`;
    constructor(private http: HttpClient) { }

    loadProducts(
        page: number = 0,
        searchQuery: string = '',
        filters: {
            supermarket?: string;
            minPrice?: string;
            maxPrice?: string;
        } = {}
    ): Observable<any> {
        let endpoint = `${this.apiUrl}?page=${page}&limit=${this.PRODUCTS_SIZE}&search=${encodeURIComponent(searchQuery)}`;
        if (filters.supermarket)
            endpoint += `&supermarket=${filters.supermarket}`;
        if (filters.minPrice)
            endpoint += `&minPrice=${encodeURIComponent(filters.minPrice)}`;
        if (filters.maxPrice)
            endpoint += `&maxPrice=${encodeURIComponent(filters.maxPrice)}`;

        // Check if the query has been cached
        const cachedQuery = localStorage.getItem(`products_${endpoint}`);
        if (cachedQuery) {
            const cachedData = JSON.parse(cachedQuery);
            const now = new Date().getTime();
            if (now - cachedData.timestamp < this.CACHE_TIMEOUT) {
                return new Observable((observer) => {
                    observer.next(cachedData.data);
                    observer.complete();
                });
            }
        }

        return new Observable((observer) => {
            this.http.get(endpoint).subscribe({
                next: (data) => {
                    localStorage.setItem(`products_${endpoint}`, JSON.stringify({
                        timestamp: new Date().getTime(),
                        data: data
                    }));
                    observer.next(data);
                    observer.complete();
                },
                error: (err) => {
                    console.error('Error loading products', err);
                    observer.error(err);
                }
            });
        });
    }

    getProductById(productId: string): Observable<any> {
        const endpoint = `${this.apiUrl}/${encodeURIComponent(productId)}`;
        return this.http.get(endpoint);
    }

    getRelatedProducts(productId: string): Observable<Response<Product[]>> {
        const endpoint = `${this.apiUrl}/${encodeURIComponent(productId)}/suggested`;

        return new Observable((observer) => {
            this.http.get<Response<Product[]>>(endpoint).subscribe({
            next: (res) => {
                observer.next({ data: res.data.slice(0, 4), error: null });
                observer.complete();
            },
            error: (err) => {
                console.error('Error loading related products', err);
                observer.error(err);
            }
            });
        });
    }

    compareProducts(productId: string): Observable<any> {
        const endpoint = `${this.apiUrl}/${encodeURIComponent(
            productId
        )}/compare`;
        return this.http.get(endpoint);
    }
}
