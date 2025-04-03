import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '@environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private API_URL = `${environment.baseApiUrl}`
    http = inject(HttpClient);
    router = inject(Router);

    login(credentials: { username: string, password: string }) {
        this.http.post(`${this.API_URL}/auth/login`, credentials, { 
            observe: 'response',
            headers: {
                'Content-Type': 'application/json',
            }
         })
        .subscribe({
            next: (response) => {
                const token = response.headers.get('Authorization')?.split(' ')[1];
                if (token) {
                    console.log('Token received:', token);
                    localStorage.setItem('is_logged_in', 'true');
                    this.router.navigate(['/']);
                } else {
                    console.error('No token received');
                }
            },
            error: (err) => {
                console.error('Login failed', err);
            }
        });

    }

    logout() {
        this.http.post(`${this.API_URL}/auth/logout`, {}).subscribe({
            next: () => {
                console.log('Logout successful');
                localStorage.removeItem('is_logged_in');
                this.router.navigate(['/login']);
            },
            error: (err) => {
                console.error('Logout failed', err);
            }
        });
    }

    isLoggedIn(): boolean {
        const token = localStorage.getItem('is_logged_in');
        return !!token;
    }
}
