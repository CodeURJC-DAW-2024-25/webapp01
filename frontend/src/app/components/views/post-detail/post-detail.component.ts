import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { Post } from '@/types/Posts';
import { ActivatedRoute } from '@angular/router';
import { PostsService } from '@services/post.service';
import { UserDataService } from '@services/user-data.service';
import { environment } from '@environments/environment';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { CommentsService } from '@/services/comment.service';
import { PageRequest } from '@/types/common/PageRequest';
import { Comment } from '@/types/Comment';

@Component({
	selector: 'app-post-detail',
	templateUrl: './post-detail.component.html',
	styleUrl: './post-detail.component.css'
})
export class PostDetailComponent implements OnInit {
	private sanitizer: DomSanitizer = inject(DomSanitizer);
	userData: UserDataService = inject(UserDataService);
	postsService: PostsService = inject(PostsService);
	commentsService: CommentsService = inject(CommentsService);
	route: ActivatedRoute = inject(ActivatedRoute);

	currentPageRequest: PageRequest = {
		page: 0,
		size: 3
	};

	postId: number | null = null;
	post: Post | null = null;
	postContent: SafeHtml | null = null;
	postComments: Comment[] | [] = [];
	
	isLoading = Infinity;
	isLastPage = false;
	error: string | null = null;

	fetchPostDetail(id: number): void {
		this.isLoading = 2;
		this.postsService.getPostById(id).subscribe({
			next: (response) => {
				this.post = response.data;
                this.isLoading--;
			},
			error: (error) => {
                this.error = error.error.message;
				console.error('Error fetching post detail:', error);
			}
		});
		this.postsService.getPostContentById(id).subscribe({
            next: (response) => {
                this.postContent = this.sanitizer.bypassSecurityTrustHtml(response);
                this.isLoading--;
			},
			error: (error) => {
				this.error = error.error.message;
				console.error('Error fetching post content:', error);
			}
		});
	}

	fetchPostComments(): void {
		if (!this.postId) return;

		this.commentsService.getPostComments(this.postId, this.currentPageRequest).subscribe({
			next: (response) => {
				const data = response.data;
				this.postComments = [...this.postComments, ...data.page];
				this.isLastPage = data.is_last_page;
				this.currentPageRequest.page += 1;
			},
			error: (error) => {
				this.error = error.error.message;
				console.error('Error fetching post comments:', error);
			}
		});
	}

	getBannerUrl(): string {
		return `${environment.baseApiUrl}/v1/posts/${this.post?.id}/banner`;
	}

	setDefaultImage(event: Event): void {
		const target = event.target as HTMLImageElement;
		target.src = "/assets/images/template_image.png";
		target.alt = "Default Image";
	}
	
	ngOnInit(): void {
		const idParam = this.route.snapshot.paramMap.get('id');
		this.postId = idParam ? parseInt(idParam, 10) : null;

		this.fetchPostDetail(this.postId!);
		this.fetchPostComments();
	}
}
