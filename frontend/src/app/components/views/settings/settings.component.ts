import { AuthService, AuthState } from "@/services/auth.service";
import { UsersService } from "@/services/user.service";
import { ModifyUser, UserPassword } from "@/types/User";
import { getDefaultAvatar } from "@/utils/defaultImage";
import { Component, inject, OnInit } from "@angular/core";
import { isValidUserModify, validatePasswordUpdate, ResultCode } from "@/utils/validationUtils";

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
    const validationResult = isValidUserModify(this.userData.modifyUser);
    if (validationResult !== ResultCode.OK) {
      console.error(validationResult);
      this.errorMessage = validationResult;
      return;
    }
    this.userService.modifyUserData(this.userData.id, this.userData.modifyUser); 
  }

  modifyPassword(): void {
    this.errorMessagePassword = '';
    console.log(this.userData.userPassword);
    console.log(this.userData.userPassword.oldPassword);
    const passwordValidationResult = validatePasswordUpdate(this.userData.userPassword, this.userData.userPassword.oldPassword);
    if (passwordValidationResult !== ResultCode.OK) {
      console.error(passwordValidationResult);
      this.errorMessagePassword = passwordValidationResult;
      return;
    }
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
