import { Component, input } from '@angular/core';

@Component({
  selector: 'app-post-card',
  templateUrl: './post-card.component.html',
  styleUrl: './post-card.component.css'
})
export class PostCardComponent {
  id = input<number>();
  title = input<string>();
  description = input<string>();
  tags = input<string[]>();
  date = input<string>();
  readingTime = input<string>();
  extendedClass = input<string>();
}
