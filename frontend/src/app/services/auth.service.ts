import { AuthResponse } from '@/types/common/AuthResponse';
import { GlobalUser, User } from '@/types/User';
import { getUserAvatar } from '@/utils/defaultImage';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '@environments/environment';
import { UserDataService } from '@services/user-data.service';
import { BehaviorSubject, catchError, map, Observable, of } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private API_URL = `${environment.baseApiUrl}`
    http = inject(HttpClient);
    router = inject(Router);
    userDataService = inject(UserDataService);

    constructor() {
        this.uploadUserFromSessionStorage();
    }

    // Logged user to be used in the app
    private globalUser = new BehaviorSubject<GlobalUser | null>(null);
    globalUser$ = this.globalUser.asObservable();

    login(credentials: { username: string, password: string }) {
        const builtUrl = `${this.API_URL}/auth/login`;
        this.http.post(builtUrl, credentials, {
            observe: 'response',
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .subscribe({
                next: (response) => {
                    this.setUserData(response.body as AuthResponse);
                    this.router.navigate(['/']);
                },
                error: (err) => {
                    console.error('Login failed', err);
                }
            });
    }

    logout() {
        const builtUrl = `${this.API_URL}/auth/logout`;
        this.http.post(builtUrl, {}).subscribe({
            next: () => {
                this.clearUserData();
                this.router.navigate(['/login']);
            },
            error: (err) => {
                console.error('Logout failed', err);
            }
        });
    }

    checkAuth(): Observable<AuthResponse> {
        const builtUrl = `${this.API_URL}/auth/check-session`;
        return this.http.post(builtUrl, {}, {
            observe: 'response',
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
            }
        }).pipe(
            map((response) => {
                const authRes: AuthResponse = response.body as AuthResponse;
                this.setUserData(authRes);
                return authRes;
            }),
            catchError((err) => {
                console.error('Session check failed', err);
                return of({ authenticated: false } as AuthResponse);
            })
        );
    }
    
    private setUserData(data: AuthResponse): void {
        const userData: GlobalUser = {
            user: data.user,
            isAuthenticated: data.authenticated,
            isAdmin: data.user?.role === "ADMIN" || false,
            avatar: getUserAvatar(data.user),
        }
        sessionStorage.setItem('user', JSON.stringify(userData));
        this.globalUser.next(userData);
    }

    private clearUserData(): void {
        sessionStorage.removeItem('user');
        this.globalUser.next(null);
    }

    private uploadUserFromSessionStorage(): void {
        const user = sessionStorage.getItem('user');
        if (user) {
            const parsedUser = JSON.parse(user) as GlobalUser;
            this.globalUser.next(parsedUser);
        } else {
            this.globalUser.next(null);
        }
    }
}
