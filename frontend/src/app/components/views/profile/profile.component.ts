import { Component, OnInit } from '@angular/core';
import { AuthService, AuthState } from "@/services/auth.service";
import { GlobalUser } from "@/types/User";
import { getDefaultAvatar} from '@/utils/defaultImage';
import { ShoppingListService } from '@/services/shoppingList.service';
@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
    user: GlobalUser | null = null;
    shoppingList: any[] = [];
    
    constructor(private authService: AuthService, private shoppingListService: ShoppingListService ) {}

    ngOnInit(): void {
        this.authService.authState$.subscribe((authState: AuthState) => {
            this.user = authState.user;
            if (this.user?.user?.id) {
                this.loadShoppingLists(this.user.user.id);
            }
        });
    }
    
    loadShoppingLists(userId:number): void {
      this.shoppingListService.getUserLists(userId).subscribe({
          next: (response) => {
              console.log('Listas de compras:', response.data.content);
              this.shoppingList = response.data.content; 
          },
          error: (error) => {
              console.error('Error al cargar las listas:', error);
              },
      });
  }
    editProfile(): void {
        console.log('TODO Editar perfil');
    }

    logout(): void {
        this.authService.logout();
    }
        
    newlist(): void {
        console.log('Abrir modal para nueva lista');
    }

    setDefaultImage(event: Event): void {
        const target = event.target as HTMLImageElement;
        target.src = getDefaultAvatar();
        target.alt = "Default Image";
      }
      
}
