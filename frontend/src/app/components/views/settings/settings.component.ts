import { AuthService, AuthState } from "@/services/auth.service";
import { UsersService } from "@/services/user.service";
import { SettingsUser } from "@/types/User";
import { getDefaultAvatar } from "@/utils/defaultImage";
import { Component, inject, OnInit } from "@angular/core";


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
    email: '',
    username: '',
    name: '',
    password: '',
    confirmPassword: '',
    newPassword: '',
    avatar: this.avatar
  };

  private authService = inject(AuthService);
  private userService = inject(UsersService);

  getDefaultAvatar(event: Event): void {
    const target = event.target as HTMLImageElement;
    target.src = getDefaultAvatar();
    target.alt = "Default Avatar";
  }

  ngOnInit(): void {
    this.authService.authState$.subscribe((authState: AuthState) => {
      this.isLoading = authState.isLoading;
      this.isAuthenticated = !!authState.user?.isAuthenticated;
      this.avatar = authState.user?.avatar || getDefaultAvatar();
      if (!this.isLoading && authState.user?.id) {
        this.userService.getUserEmail(authState.user?.id).subscribe((res) => {
          this.userData = {
            email: res.data,
            username: authState.user?.user?.username || '',
            name: authState.user?.user?.name || '',
            password: '',
            confirmPassword: '',
            newPassword: '',
            avatar: this.avatar
          };
        });
      }
    });
  }
}
