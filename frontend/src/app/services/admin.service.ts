import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageRequest } from '@/types/common/PageRequest';


@Injectable({
  providedIn: 'root'
})

export class AdminService {
    private apiUrl = 'https://localhost:8443/api/v1/users?page=0&size=4.'

    constructor(private http: HttpClient) {}

    getUsers(pageRequest: PageRequest): Observable<any> {
      return this.http.get<any>(`${this.apiUrl}/users?page=${pageRequest.page}&size=${pageRequest.size}`);
    }



}

