import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { PaginatedResponse } from '@/types/common/PaginatedResponse';
import { ShoppingList } from '@/types/ShoppingList';
import { Response } from '@/types/common/Response';

@Injectable({
    providedIn: 'root',
})
export class ShoppingListService {
    private apiUrl = `${environment.baseApiUrl}/v1/lists`;

    constructor(private http: HttpClient) {}

    getUserLists(userId: number): Observable<PaginatedResponse<ShoppingList>> {
        const endpoint = `${this.apiUrl}`;
        return this.http.get<PaginatedResponse<ShoppingList>>(endpoint, {
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
            },
        });
    }

    getShoppingListById(listId: number): Observable<Response<ShoppingList>> {
        const endpoint = `${this.apiUrl}/${listId}`;
        return this.http.get<Response<ShoppingList>>(endpoint, {
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
            },
        });
    }

    deleteShoppingList(listId: number): Observable<any> {
        const endpoint = `${this.apiUrl}/${listId}`;
        return this.http.delete(endpoint, {
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
            },
        });
    }
    createShoppingList(request: {
        name: string;
        description: string;
    }): Observable<any> {
        const endpoint = `${this.apiUrl}`;
        return this.http.post(endpoint, request, {
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
            },
        });
    }

    addProductToList(listId: number, productId: string): Observable<any> {
        const endpoint = `${this.apiUrl}/${listId}/product/${productId}`;
        return this.http.post(endpoint, null, {
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
            },
        });
    }

    removeProductFromList(listId: number, productId: string): Observable<any> {
        const endpoint = `${this.apiUrl}/${listId}/product/${productId}`;
        return this.http.delete(endpoint, {
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
            },
        });
    }
}
