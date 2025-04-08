import { Component, OnInit } from '@angular/core';
import { AuthService } from "@/services/auth.service";
import { GlobalUser } from "@/types/User";

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
    user: GlobalUser | null = null;

    constructor(private authService: AuthService) {}

    ngOnInit(): void {
        this.authService.globalUser$.subscribe((user: GlobalUser | null) => {
            this.user = user;
        });
    }

    editProfile(): void {
        console.log('TODO Editar perfil');
    }

    changePassword(): void {
        console.log('TODO Cambiar contraseña');
    }

    deleteAccount(): void {
        console.log('TODO Eliminar cuenta');
    }
    logout(): void {
        this.authService.logout();
    }
        
    openModal(): void {
        console.log('Abrir modal para nueva lista');
    }
}
