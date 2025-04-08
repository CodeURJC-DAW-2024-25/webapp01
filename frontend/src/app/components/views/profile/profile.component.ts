import { Component, OnInit } from '@angular/core';
import { AuthService, AuthState } from "@/services/auth.service";
import { GlobalUser } from "@/types/User";
import { getDefaultAvatar} from '@/utils/defaultImage';
@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
    user: GlobalUser | null = null;

    constructor(private authService: AuthService) {}

    ngOnInit(): void {
        this.authService.authState$.subscribe((authState: AuthState) => {
            this.user = authState.user;
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

    setDefaultImage(event: Event): void {
        const target = event.target as HTMLImageElement;
        target.src = getDefaultAvatar();
        target.alt = "Default Image";
      }
      
}
