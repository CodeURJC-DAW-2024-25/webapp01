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

    let isLoading = true;
    let userRole: string | null = null;

    authService.authState$.subscribe((authState) => {
        isLoading = authState.isLoading;
        userRole = authState.user?.user?.role || null;
        console.log('Auth state:', authState);
    });

    if (isLoading) {
        return of(false);
    }

    const requiredRoles: string[] = route.data['roles'] || [];
    const isAuthorized = requiredRoles.length === 0
        || (userRole !== null
        && requiredRoles.includes(userRole));

    if (!isAuthorized) {
        authService.logout();
        router.navigate(['/login']);
        return false;
    }

    return true;
};