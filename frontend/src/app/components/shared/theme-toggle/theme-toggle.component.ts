import { Component } from '@angular/core';

@Component({
  selector: 'app-theme-toggle',
  templateUrl: './theme-toggle.component.html',
  styleUrl: './theme-toggle.component.css'
})
export class ThemeToggleComponent {

  THEME_KEY = 'theme';
  DARK_CLASS = 'dark';

  setTheme(theme: string) {
    document.documentElement.classList.toggle(this.DARK_CLASS, theme === this.DARK_CLASS);
    window.localStorage.setItem(this.THEME_KEY, theme);
  }

  getStoredTheme() {
    return window.localStorage.getItem(this.THEME_KEY);
  }

  getSystemTheme() {
    return window.matchMedia('(prefers-color-scheme: dark)').matches ? this.DARK_CLASS : 'light';
  }

  retrieveTheme() {
    const storedTheme = this.getStoredTheme();
    const theme = storedTheme ? storedTheme : this.getSystemTheme();
    this.setTheme(theme);
  }

  toggleTheme(){
    const newTheme = document.documentElement.classList.contains(this.DARK_CLASS) ? 'light' : this.DARK_CLASS;
    this.setTheme(newTheme);
  }

}
