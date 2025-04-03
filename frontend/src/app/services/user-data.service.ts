import { User } from '@/types/User';
import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserDataService {
  // --- Properties ---
  private _isAuthenticated = signal(false);
  private _user = signal<User | null>(null);
  private _avatar = signal('');
  private _isAdmin = signal(false);

  constructor() { }

  // --- Methods ---
  get isAuthenticated(): boolean {
    return this._isAuthenticated();
  }
  set isAuthenticated(value: boolean) {
    this._isAuthenticated.set(value);
  }

  get user(): User | null {
    return this._user();
  }
  set user(value: User | null) {
    this._user.set(value);
  }

  get avatar(): string {
    return this._avatar();
  }
  set avatar(value: string) {
    this._avatar.set(value);
  }

  get isAdmin(): boolean {
    return this._isAdmin();
  }
  set isAdmin(value: boolean) {
    this._isAdmin.set(value);
  }
}
