import { Component } from '@angular/core';
import { Post } from '../../../types/Posts';
import { PostsService } from '../../../services/posts/post.service';
import { PageRequest } from '../../../types/common/PageRequest';

@Component({
	selector: 'app-posts',
	templateUrl: './posts.component.html',
	styleUrl: './posts.component.css'
})
export class PostsComponent {
	currentPageRequest: PageRequest = {
		page: 0,
		size: 4
	};
	posts: Post[] = [];
	isLoading = true;
	isLastPage = false;
	error: string | null = null;

	constructor(private postsService: PostsService) { }

	fetchPosts(): void {
		this.isLoading = true;
		this.postsService.getPosts(this.currentPageRequest).subscribe({
			next: (response) => {
				this.posts = this.posts.concat(response.data.page);
				this.isLastPage = response.data.is_last_page;
				this.isLoading = false;
				this.currentPageRequest.page += 1;
			},
			error: (error) => {
				this.isLoading = false;
				this.error = error.error.message;
				console.error('Error fetching posts:', error);
			}
		});
	}

	ngOnInit(): void {
		this.fetchPosts();
	}
}
