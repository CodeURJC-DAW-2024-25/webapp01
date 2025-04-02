import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { Post } from '@/types/Posts';
import { ActivatedRoute } from '@angular/router';
import { PostsService } from '@services/post.service';
import { UserDataService } from '@services/user-data.service';
import { environment } from '@environments/environment';
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

	async fetchPostDetail(id: string): Promise<void> {
		this.isLoading = true;
		await this.postsService.getPostById(id).subscribe({
			next: (response) => {
				this.post.set(response.data);
			},
			error: (error) => {
				this.isLoading = false;
				this.error = error.error.message;
				console.error('Error fetching post detail:', error);
			}
		});
		await this.postsService.getPostContentById(id).subscribe({
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

        this.isLoading = false;
	}

	getBannerUrl(): string {
		return `${environment.baseApiUrl}/posts/${this.post()?.id}/banner`;
	}

	setDefaultImage(event: Event): void {
		const target = event.target as HTMLImageElement;
		console.log(target);
		target.src = "/assets/images/template_image.png";
		target.alt = "Default Image";
	}
	
	ngOnInit(): void {
		this.postId = this.route.snapshot.paramMap.get('id');
		this.fetchPostDetail(this.postId!);
	}
}
