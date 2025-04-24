import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageRequest } from '@/types/common/PageRequest';
import { PaginatedResponse } from '@/types/common/PaginatedResponse';
import { User, ModifyUser, UserPassword } from '@/types/User';
import { Response } from '@/types/common/Response';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { environment } from '@environments/environment';


@Injectable({
    providedIn: 'root'
})
export class UsersService {
    private apiUrl = `${environment.baseApiUrl}/v1/users`;
    http = inject(HttpClient);
    router = inject(Router);
    authService = inject(AuthService);

    getUsers(pageRequest: PageRequest): Observable<PaginatedResponse<User>> {
        const builtUrl = `${this.apiUrl}?page=${pageRequest.page}&size=${pageRequest.size}`;
        return this.http.get<PaginatedResponse<User>>(builtUrl);
    }

    getUserEmail(userId: number): Observable<Response<string>> {
        const builtUrl = `${this.apiUrl}/${userId}/email`;
        return this.http.get<Response<string>>(builtUrl, {
            withCredentials: true
        });
    }

    uploadAvatar(userId: number, file: File): Observable<Response<User>> {
        const builtUrl = `${this.apiUrl}/${userId}/avatar`;
        const formData = new FormData();
        formData.append('avatar', file);
        return new Observable((observer) => {
            this.http.post<Response<User>>(builtUrl, formData, {
                withCredentials: true
            }).subscribe({
                next: (res) => {
                    this.authService.modifyUserData(res.data);
                    observer.next(res);
                    observer.complete();
                }
                , error: (err) => {
                    console.error(err);
                    observer.error(err);
                }
            });
        });
    }

    modifyUserData(userId: number, modifyUser: ModifyUser): Observable<any> {
        const builtUrl = `${this.apiUrl}/${userId}`;
        return new Observable((observer) => {
            this.http.patch<Response<User>>(builtUrl, modifyUser, {
                withCredentials: true
            }).subscribe({
                next: (res) => {
                    this.authService.modifyUserData(res.data);
                    observer.next(res);
                    observer.complete();
                }
                , error: (err) => {
                    console.error(err);
                    observer.error(err);
                }
            });
        });
    }

    modifyUserPassword(userId: number, userPassword: UserPassword): Observable<any> {
        const builtUrl = `${this.apiUrl}/${userId}/password`;
        return this.http.patch<any>(builtUrl, userPassword, {
            withCredentials: true
        });
    }

    deleteAccount(userId: number): void {
        const builtUrl = `${this.apiUrl}/${userId}`;
        this.http.delete<any>(builtUrl, {
            withCredentials: true
        }).subscribe({
            next: (res) => {
                this.authService.clearUserData();
                this.router.navigate(['/']);
            }
            , error: (err) => {
                console.error(err);
            }
        });
    }

    deleteUser(userId: number): Observable<any> {
        const builtUrl = `${this.apiUrl}/${userId}`;
        return this.http.delete<any>(builtUrl, {
            withCredentials: true
        });
    }
}

