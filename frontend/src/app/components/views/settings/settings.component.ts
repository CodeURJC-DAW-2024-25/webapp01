import { AuthService, AuthState } from "@/services/auth.service";
import { UsersService } from "@/services/user.service";
import { ModifyUser, UserPassword } from "@/types/User";
import { getDefaultAvatar } from "@/utils/defaultImage";
import { Component, inject, OnInit, Output } from "@angular/core";
import { isValidUserModify, validatePasswordUpdate } from "@/utils/validationUtils";
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
  @Output() launchPopup: boolean = false;
  popupHeader: string = '';
  popupContent: string = '';
  avatar: string = getDefaultAvatar();
  errors : SettingsUser = {
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
  errorMessage: string = '';
  errorMessagePassword: string = '';
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


  onClosePopup(): void {
    this.launchPopup = false;
  }

  getDefaultAvatar(event: Event): void {
    const target = event.target as HTMLImageElement;
    target.src = getDefaultAvatar();
    target.alt = "Default Avatar";
  }

  uploadAvatar(): void {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.addEventListener('change', (event: Event) => {
      const input = event.target as HTMLInputElement;
      if (input.files && input.files.length > 0) {
        const file = input.files[0];
        this.userService.uploadAvatar(this.userData.id,file).subscribe({
          next: (res) => {
            this.avatar = this.avatar + `?t=${new Date().getTime()}`;
            window.dispatchEvent(new Event('updateAvatar'));
          }
        });
      }
    });
    fileInput.click();
  }

  modifyUserData(): void {
    this.errorMessage = '';
    this.errors.modifyUser = isValidUserModify(this.userData.modifyUser);
    if (this.errors.modifyUser.name == '' && this.errors.modifyUser.username == '' && this.errors.modifyUser.email == '') {
    this.userService.modifyUserData(this.userData.id, this.userData.modifyUser).subscribe({
      next: () => {
        this.popupHeader = 'Cambios Guardados';
        this.popupContent = 'Usuario modificado correctamente';
        this.launchPopup = true;
      },
      error: (res) => {
        this.errorMessage = res.error.error.message;
        console.error(res);
      }
    }); 
  }
}

  modifyPassword(): void {
    this.errorMessagePassword = '';
    this.errors.userPassword = validatePasswordUpdate(this.userData.userPassword);
    if (this.errors.userPassword.oldPassword == '' && this.errors.userPassword.newPassword == '' && this.errors.userPassword.newPasswordConfirmation == '') {
      this.userService.modifyUserPassword(this.userData.id, this.userData.userPassword).subscribe({
        next: () => {
          this.popupHeader = 'Cambios Guardados';
          this.popupContent = 'Contraseña modificada correctamente';
          this.launchPopup = true;
        },
        error: (res) => {
          this.errorMessagePassword = res.error.error.message;
          console.error(res);
        }
      });
    } 
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
