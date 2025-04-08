import { AuthResponse } from '@/types/common/AuthResponse';
import { GlobalUser, User } from '@/types/User';
import { getUserAvatar } from '@/utils/defaultImage';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '@environments/environment';
import { UserDataService } from '@services/user-data.service';
import { BehaviorSubject, catchError, map, Observable, of } from 'rxjs';

export interface AuthState {
    isLoading: boolean;
    user: GlobalUser | null;
}

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
    private authState = new BehaviorSubject<AuthState>({
        isLoading: true,
        user: null
    })

    authState$ = this.authState.asObservable();

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
                this.router.navigate(['/']);
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
        this.authState.next({
            isLoading: false,
            user: userData
        });
    }

    private clearUserData(): void {
        sessionStorage.removeItem('user');
        this.authState.next({
            isLoading: false,
            user: null
        });
    }

    private uploadUserFromSessionStorage(): void {
        const user = sessionStorage.getItem('user');
        if (user) {
            this.checkAuth().subscribe({
                next: (response) => {
                    if (response.authenticated) {
                        const parsedUser = JSON.parse(user) as GlobalUser;
                        this.authState.next({
                            isLoading: false,
                            user: {
                                ...parsedUser,
                            }
                        });
                    } else {
                        this.clearUserData();
                    }
                }
            });
        } else {
            this.authState.next({
                isLoading: false,
                user: null
            });
        }
    }
}
