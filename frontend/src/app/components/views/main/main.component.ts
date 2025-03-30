import { Component, inject, OnInit } from '@angular/core';
import { Meta, Title } from '@angular/platform-browser';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css', '../../../styles/index.css' ],
})
export class MainComponent implements OnInit {
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
