import { Component, inject, input } from '@angular/core';
import { UserDataService } from '../../../services/templates/user-data.service';

@Component({
  selector: 'app-posts-preview',
  templateUrl: './posts-preview.component.html',
  styleUrl: './posts-preview.component.css'
})
export class PostsPreviewComponent {
  // --- Dependency Injection ---
  userData: UserDataService = inject(UserDataService);
  posts: any[] = [];  // TODO: Implement the Post service and replace 'any' with the actual Post type
  postCards: any[] = [{
    id: 1,
    title: 'Post Title 1',
    description: 'Post description 1',
    tags: ['tag1', 'tag2'],
    date: '2023-10-01',
    readingTime: '5 min',
    extendedClass: 'extended-class-1'
  }]; // TODO: Implement the PostCard type and replace 'any' with the actual PostCard type

  // --- Properties ---

  // --- Methods ---
}
