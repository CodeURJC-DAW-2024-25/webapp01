import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
    providedIn: 'root',
})
export class ShoppingListService {
    private apiUrl = `${environment.baseApiUrl}/v1/lists`;

    constructor(private http: HttpClient) {}

    getUserLists(userId: number): Observable<any> {
        const endpoint = `${this.apiUrl}`;
        console.log('Endpoint:', endpoint);
        return this.http.get(endpoint, {
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
            }
        });
    }

    getShoppingListById(listId: number): Observable<any> {
        const endpoint = `${this.apiUrl}/${listId}`;
        return this.http.get(endpoint, {
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
}   