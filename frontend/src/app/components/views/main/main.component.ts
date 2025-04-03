import { Component } from "@angular/core";
import { PostsService } from "@services/post.service";
import { Post } from "@/types/Posts";
import { PageRequest } from "@/types/common/PageRequest";

@Component({
	selector: 'app-main',
	templateUrl: './main.component.html',
	styleUrls: ['./main.component.css'],
})
export class MainComponent {
	posts: Post[] = [];
	isLoading = true;
	error: string | null = null;

	constructor(private postsService: PostsService) { }

	ngOnInit(): void {
		const pageRequest: PageRequest = { page: 0, size: 4 };
		this.postsService.getPosts(pageRequest).subscribe({
			next: (response) => {
				this.posts = response.data.page;
				this.isLoading = false;
			},
			error: (err) => {
				this.error = err.message;
				console.error('Error fetching posts:', err);
			},
		});
	}
}
