import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserDataService {
  // --- Properties ---
  private isAuthenticated: boolean = false;
  private avater: string = '';

  constructor() { }

  // --- Methods ---
  get IsAuthenticated(): boolean {
    return this.isAuthenticated;
  }
  set IsAuthenticated(value: boolean) {
    this.isAuthenticated = value;
  }

  get Avater(): string {
    return this.avater;
  }
  set Avater(value: string) {
    this.avater = value;
  }
}
