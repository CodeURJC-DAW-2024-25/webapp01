import { Component, inject, Input, input } from '@angular/core';
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

	// --- Properties ---
	@Input() posts: Post[] = [];
	@Input() extendedClass: string = '';

	// --- Methods ---
}
