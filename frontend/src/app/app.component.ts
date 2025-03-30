import { Component, inject, OnInit } from '@angular/core';
import { Meta, Title } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  standalone: false,
})
export class AppComponent implements OnInit {
  // --- Dependency Injection ---
  private meta: Meta = inject(Meta);
  private titleService: Title = inject(Title);

  // --- Properties ---

  // --- Methods ---
  ngOnInit(): void {
    this.meta.addTag({ name: 'description', content: 'This is the main component of the Angular application.' });
    this.titleService.setTitle('SaveX - Ahorra dinero, tiempo y esfuerzo');
  }
}
