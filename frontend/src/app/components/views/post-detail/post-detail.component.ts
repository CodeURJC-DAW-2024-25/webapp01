import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { Post } from '@/types/Posts';
import { ActivatedRoute } from '@angular/router';
import { PostsService } from '@services/post.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { CommentsService } from '@/services/comment.service';
import { PageRequest } from '@/types/common/PageRequest';
import { Comment, CreateCommentRequest } from '@/types/Comment';
import { getDefaultImage, getPostBanner } from '@/utils/defaultImage';
import { AuthService, AuthState } from '@/services/auth.service';

@Component({
	selector: 'app-post-detail',
	templateUrl: './post-detail.component.html',
	styleUrl: './post-detail.component.css'
})
export class PostDetailComponent implements OnInit {
	private sanitizer: DomSanitizer = inject(DomSanitizer);
	authService: AuthService = inject(AuthService);
	postsService: PostsService = inject(PostsService);
	commentsService: CommentsService = inject(CommentsService);
	route: ActivatedRoute = inject(ActivatedRoute);

	currentPageRequest: PageRequest = {
		page: 0,
		size: 3
	};

	private _userData = {
		isLoading: true,
		isAuthenticated: false,
		isAdmin: false
	}

	postId: number | null = null;
	post: Post | null = null;
	postContent: SafeHtml | null = null;
	postComments: Comment[] = [];
	
	isLoading = Infinity;
	isLastPage = false;
	error: string | null = null;

	commentText: string = '';

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

	submitComment(): void {
		const commentRequest: CreateCommentRequest = {
			postId: this.postId!,
			content: this.commentText
		}

		this.commentsService.createPostComment(commentRequest).subscribe({
			next: (response) => {
				this.postComments = [response.data, ...this.postComments];
				this.commentText = '';
			},
			error: (error) => {
				this.error = error.error.message;
				console.error('Error submitting comment:', error);
			}
		});
	}

	getBannerUrl(): string {
		return getPostBanner(this.post);
	}

	setDefaultImage(event: Event): void {
		const target = event.target as HTMLImageElement;
		target.src = getDefaultImage();
		target.alt = "Default Image";
	}
	
	ngOnInit(): void {
		const idParam = this.route.snapshot.paramMap.get('id');
		this.postId = idParam ? parseInt(idParam, 10) : null;

		this.authService.authState$.subscribe((authState: AuthState) => {
			this._userData.isLoading = authState.isLoading;
			this._userData.isAuthenticated = !!authState.user?.isAuthenticated;
			this._userData.isAdmin = authState.user?.isAdmin || false;
		});

		this.fetchPostDetail(this.postId!);
		this.fetchPostComments();
	}

	get userData() {
		return this._userData;
	}

	set userData(value) {
		this._userData = value;
	}
}
