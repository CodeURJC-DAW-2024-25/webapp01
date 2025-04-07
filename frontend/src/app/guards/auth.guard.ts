import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@services/auth.service';
import { CanActivateFn } from '@angular/router';
import { of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { AuthResponse } from '@/types/common/AuthResponse';


export const authGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    return authService.checkAuth().pipe(
        map((response: AuthResponse) => {
            console.log('Auth response:', response);
            const requiredRoles: string[] = route.data['roles'] || [];
            const userRole = response.user?.role || null;
            const isAuthorized = requiredRoles.length === 0 || (userRole !== null && requiredRoles.includes(userRole));

            if (!isAuthorized) {
                authService.logout();
                router.navigate(['/login']);
                return false;
            }

            return true;
        }),
        catchError(() => {
            authService.logout();
            router.navigate(['/login']);
            return of(false);
        })
    );
};