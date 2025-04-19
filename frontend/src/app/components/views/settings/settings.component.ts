import { AuthService, AuthState } from "@/services/auth.service";
import { UsersService } from "@/services/user.service";
import { ModifyUser, UserPassword } from "@/types/User";
import { getDefaultAvatar } from "@/utils/defaultImage";
import { Component, inject, OnInit } from "@angular/core";

interface SettingsUser{
  id: number;
  modifyUser: ModifyUser;
  userPassword: UserPassword;
}


@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent implements OnInit {
  isAuthenticated: boolean = false;
  isLoading: boolean = true;
  avatar: string = getDefaultAvatar();
  userData : SettingsUser = {
    id: -1,
    modifyUser: {
      name: '',
      username: '',
      email: ''
    },
    userPassword: {
      oldPassword: '',
      newPassword: '',
      newPasswordConfirmation: ''
    }
  };

  private authService = inject(AuthService);
  private userService = inject(UsersService);

  getDefaultAvatar(event: Event): void {
    const target = event.target as HTMLImageElement;
    target.src = getDefaultAvatar();
    target.alt = "Default Avatar";
  }

  modifyUserData(): void {
    this.userService.modifyUserData(this.userData.id,this.userData.modifyUser); 
  }

  modifyPassword(): void {
    this.userService.modifyUserPassword(this.userData.id,this.userData.userPassword);
  }

  deleteAccount(): void {
    this.userService.deleteAccount(this.userData.id);
  }

  ngOnInit(): void {
    this.authService.authState$.subscribe((authState: AuthState) => {
      this.isLoading = authState.isLoading;
      this.isAuthenticated = !!authState.user?.isAuthenticated;
      this.avatar = authState.user?.avatar || getDefaultAvatar();
      if (!this.isLoading && authState.user?.id) {
        this.userService.getUserEmail(authState.user?.id).subscribe((res) => {
          this.userData = {
            id: authState.user?.id || -1,
            modifyUser: {
              name: authState.user?.user?.name || '',
              username: authState.user?.user?.username || '',
              email: res.data,
            },
            userPassword: {
              oldPassword: '',
              newPassword: '',
              newPasswordConfirmation: ''
            },
          };
        });
      }
    });
  }
}
