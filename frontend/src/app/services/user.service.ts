import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageRequest } from '@/types/common/PageRequest';
import { PaginatedResponse } from '@/types/common/PaginatedResponse';
import { User } from '@/types/User';


@Injectable({
  providedIn: 'root'
})
export class UsersService {
    private apiUrl = 'https://localhost:8443/api/v1/users'

    constructor(private http: HttpClient) {}

    getUsers(pageRequest: PageRequest): Observable<PaginatedResponse<User>> {
      const builtUrl = `${this.apiUrl}?page=${pageRequest.page}&size=${pageRequest.size}`;
      return this.http.get<PaginatedResponse<User>>(builtUrl);
    }

}

