import { AuthResponse } from '@/types/common/AuthResponse';
import { GlobalUser, User } from '@/types/User';
import { getUserAvatar } from '@/utils/defaultImage';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '@environments/environment';
import { UserDataService } from '@services/user-data.service';
import { BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private API_URL = `${environment.baseApiUrl}`
    http = inject(HttpClient);
    router = inject(Router);
    userDataService = inject(UserDataService);

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

    checkAuth() {
        const builtUrl = `${this.API_URL}/auth/check-session`;
        this.http.post(builtUrl, {}, {
            observe: 'response',
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .subscribe({
            next: (response) => {
                const authRes: AuthResponse = response.body as AuthResponse;
                this.setUserData(authRes);
            },
            error: (err) => {
                console.error('Session check failed', err);
            }
        });
    }

    private setUserData(data: AuthResponse): void {
        const userData: GlobalUser = {
            user: data.user,
            isAuthenticated: data.authenticated,
            isAdmin: data.user?.role === "ADMIN" || false,
            avatar: getUserAvatar(data.user),
        }
        this.globalUser.next(userData);
    }

    private clearUserData(): void {
        this.globalUser.next(null);
    }
}
