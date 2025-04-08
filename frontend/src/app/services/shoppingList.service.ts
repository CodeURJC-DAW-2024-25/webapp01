import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
    providedIn: 'root',
})
export class ShoppingListService {
    private apiUrl = `${environment.baseApiUrl}/v1/shoppingList`;

    constructor(private http: HttpClient) {}

    getUserLists(userId: number): Observable<any> {
        const endpoint = `${this.apiUrl}/${userId}`;
        return this.http.get(endpoint);
    }
}