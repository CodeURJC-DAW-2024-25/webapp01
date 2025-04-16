import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StatsService {
  private apiUrl = `${environment.baseApiUrl}/v1`;

  constructor(private http: HttpClient) { }

  getProductsStats(): Observable<any> {
    const builtUrl = `${this.apiUrl}/stats/products`;
    return this.http.get<any>(builtUrl);
  }
}