import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageRequest } from '@/types/common/PageRequest';
import { PaginatedResponse } from '@/types/common/PaginatedResponse';
import { User, ModifyUser, UserPassword } from '@/types/User';
import { Response } from '@/types/common/Response';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { AuthResponse } from '@/types/common/AuthResponse';


@Injectable({
  providedIn: 'root'
})
export class UsersService {
    private apiUrl = 'https://localhost:8443/api/v1/users'
    http = inject(HttpClient);
    router = inject(Router);
    authservice = inject(AuthService);

    getUsers(pageRequest: PageRequest): Observable<PaginatedResponse<User>> {
      const builtUrl = `${this.apiUrl}?page=${pageRequest.page}&size=${pageRequest.size}`;
      return this.http.get<PaginatedResponse<User>>(builtUrl);
    }

    getUserEmail(userid: number): Observable<Response<string>> {
      const builtUrl = `${this.apiUrl}/${userid}/email`;
      return this.http.get<Response<string>>(builtUrl, {
        withCredentials: true
      });
    }

    uploadAvatar(userid: number, file: File): void {
      const builtUrl = `${this.apiUrl}/${userid}/avatar`;
      const formData = new FormData();
      formData.append('avatar', file);
      this.http.post<Response<User>>(builtUrl, formData, {
        withCredentials: true
      }).subscribe({
        next: (res) => {
          this.authservice.modifyUserData(res.data);
        }
        , error: (err) => {
          console.error(err);
        }
      });
    }

    modifyUserData(userid: number, modifyUser: ModifyUser): void{
      const builtUrl = `${this.apiUrl}/${userid}`;
      this.http.patch<Response<User>>(builtUrl, modifyUser, {
        withCredentials: true
      }).subscribe({
        next: (res) => {
          this.authservice.modifyUserData(res.data);
        }
        , error: (err) => {
          console.error(err);
        }
      });
    }

    modifyUserPassword(userid: number, userPassword: UserPassword): Observable<any> {
      const builtUrl = `${this.apiUrl}/${userid}/password`;
      return this.http.patch<any>(builtUrl, userPassword, {
        withCredentials: true
      });
    }
    
    deleteAccount(userid: number): void {
      const builtUrl = `${this.apiUrl}/${userid}`;
      this.http.delete<any>(builtUrl, {
        withCredentials: true
      }).subscribe({
        next: (res) => {
          this.authservice.clearUserData();
          this.router.navigate(['/']);
        }
        , error: (err) => {
          console.error(err);
        }
      });
    }
}

