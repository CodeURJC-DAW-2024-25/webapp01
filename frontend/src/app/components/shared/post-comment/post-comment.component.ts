import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { Comment } from '@/types/Comment';
import { environment } from '@environments/environment';
import { PageRequest } from '@/types/common/PageRequest';
import { getDefaultAvatar } from '@/utils/defaultImage';
import { CommentsService } from '@/services/comment.service';

@Component({
	selector: 'app-post-comment',
	templateUrl: './post-comment.component.html',
	styleUrl: './post-comment.component.css'
})
export class PostCommentComponent {
	@Input() postId: number | undefined = undefined;
	@Input() comment: Comment | null = null;
	@Input() allowDelete: boolean = false;

	@Output() commentDeleted = new EventEmitter<number>();

	private commentService = inject(CommentsService);

	deleteComment(): void {
		if (this.comment && this.postId) {
			this.commentService.deletePostComment({ postId: this.postId, commentId: this.comment.id }).subscribe({
				next: (response) => {
					this.commentDeleted.emit(response.data.id);
				},
				error: (error) => {
					console.error('Error deleting comment:', error);
				}
			});
		}
	}

	getAvatarUrl(): string {
		return `${environment.baseApiUrl}/users/${this.comment?.authorId}/avatar`;
	}

	setDefaultImage(event: Event): void {
		const target = event.target as HTMLImageElement;
		target.src = getDefaultAvatar();
		target.alt = "Default Avatar";
	}
}
