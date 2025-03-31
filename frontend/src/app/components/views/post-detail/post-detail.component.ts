import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { Post } from '../../../types/Posts';
import { ActivatedRoute } from '@angular/router';
import { PostsService } from '../../../services/posts/post.service';
import { UserDataService } from '../../../services/templates/user-data.service';
import { environment } from '../../../../environments/environment';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
	selector: 'app-post-detail',
	templateUrl: './post-detail.component.html',
	styleUrl: './post-detail.component.css'
})
export class PostDetailComponent implements OnInit {
	private sanitizer: DomSanitizer = inject(DomSanitizer);
	userData: UserDataService = inject(UserDataService);
	postsService: PostsService = inject(PostsService);
	route: ActivatedRoute = inject(ActivatedRoute);

	postId: string | null = null;
	post: WritableSignal<Post | null> = signal(null);
	postContent: WritableSignal<SafeHtml | null> = signal(null);
	
	isLoading: boolean = true;
	error: string | null = null;

	fetchPostDetail(id: string): void {
		this.isLoading = true;
		this.postsService.getPostById(id).subscribe({
			next: (response) => {
				this.post.set(response.data);
				this.isLoading = false;
			},
			error: (error) => {
				this.isLoading = false;
				this.error = error.error.message;
				console.error('Error fetching post detail:', error);
			}
		});
		this.postsService.getPostContentById(id).subscribe({
			next: (response) => {
				this.postContent.set(
					this.sanitizer.bypassSecurityTrustHtml(response)
				)
			},
			error: (error) => {
				this.error = error.error.message;
				console.error('Error fetching post content:', error);
			}
		});
	}

	getBannerUrl(): string {
		return `${environment.baseApiUrl}/posts/${this.post()?.id}/banner`;
	}

	ngOnInit(): void {
		this.postId = this.route.snapshot.paramMap.get('id');
		this.fetchPostDetail(this.postId!);
	}
}
