import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@services/auth.service';
import { CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    let userRole: string | null = null;

    authService.authState$.subscribe((authState) => {
        userRole = authState.user?.user?.role || null;
    });

    const requiredRoles: string[] = route.data['roles'] || [];
    const isAuthorized = requiredRoles.length === 0
        || (userRole !== null
        && requiredRoles.includes(userRole));

    if (!isAuthorized) {
        router.navigate(['/']);
        return false;
    }

    return true;
};