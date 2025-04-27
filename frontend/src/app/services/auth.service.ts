import { AuthResponse } from '@/types/common/AuthResponse';
import { GlobalUser, RegisterUser, User } from '@/types/User';
import { getUserAvatar } from '@/utils/defaultImage';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '@environments/environment';
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

    constructor() {
        this.uploadUserFromLocalStorage();
    }

    // Logged user to be used in the app
    private authState = new BehaviorSubject<AuthState>({
        isLoading: true,
        user: null
    })

    authState$ = this.authState.asObservable();

    login(credentials: { username: string, password: string }): Observable<AuthResponse> {
        const builtUrl = `${this.API_URL}/auth/login`;
        
        return new Observable<AuthResponse>(observer => { 
        this.http.post(builtUrl, credentials, {
            observe: 'response',
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
            }
        }).subscribe({
            next: (response) => {
                const res = response.body as AuthResponse;
                this.setUserData(res);
                this.router.navigate(['/']);
                observer.next(res);
                observer.complete();
            },
            error: (err) => {
                console.error('Login failed', err);
                observer.error(err);
            }

            });
        });
    }

    logout(): void {
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

    register(user: RegisterUser): Observable<any> {
        const builtUrl = `${this.API_URL}/v1/users`;
        return new Observable((observer) => {

         this.http.post(builtUrl, user, {
            observe: 'response',
            headers: {
                'Content-Type': 'application/json',
            }
        }).subscribe({
            next: (response) => {
                const authRes: AuthResponse = response.body as AuthResponse;
                this.setUserData(authRes);
                this.router.navigate(['/login']);
                observer.next(response);
                observer.complete();
            },
            error: (err) => {
                console.error('Registration failed', err);
                observer.error(err);
            }
        });
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

    isAdmin(): boolean {
        const user = this.authState.getValue().user;
        return user?.isAdmin || false;
    }

    public setUserData(data: AuthResponse): void {
        const userData: GlobalUser = {
            id: data.user?.id || null,
            user: data.user,
            isAuthenticated: data.authenticated,
            isAdmin: data.user?.role === "ADMIN" || false,
            avatar: getUserAvatar(data.user),
        }
        localStorage.setItem('user', JSON.stringify(userData));
        this.authState.next({
            isLoading: false,
            user: userData
        });
    }

    public modifyUserData(data: User): void {
        const authResponse : AuthResponse = {
            status: "SUCCESS",
            message: null,
            error: null,
            user: data,
            authenticated: true
        }
        this.setUserData(authResponse);
    }

    public clearUserData(): void {
        localStorage.removeItem('user');
        this.authState.next({
            isLoading: false,
            user: null
        });
    }

    private uploadUserFromLocalStorage(): void {
        const user = localStorage.getItem('user');
        if (user) {
            this.authState.next({
                isLoading: true,
                user: JSON.parse(user) as GlobalUser
            });

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
