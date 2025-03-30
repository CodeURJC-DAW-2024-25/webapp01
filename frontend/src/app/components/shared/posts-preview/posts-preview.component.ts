import { Component, inject, input } from '@angular/core';
import { UserDataService } from '../../../services/templates/user-data.service';
import { Post } from '../../../types/Posts';

@Component({
  selector: 'app-posts-preview',
  templateUrl: './posts-preview.component.html',
  styleUrl: './posts-preview.component.css'
})
export class PostsPreviewComponent {
  // --- Dependency Injection ---
  userData: UserDataService = inject(UserDataService);
  posts: Post[] = [{
    id: 1,
    title: 'Post Title 1',
    description: 'Post description 1',
    tags: ['tag1', 'tag2'],
    date: '2023-10-01',
    readingTime: '5 min',
    visibility: 'PUBLIC'
  }];

  // --- Properties ---

  // --- Methods ---
}
