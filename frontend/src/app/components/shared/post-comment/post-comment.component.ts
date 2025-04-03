import { Component, Input } from '@angular/core';
import { Comment } from '@/types/Comment';
import { environment } from '@environments/environment';
import { PageRequest } from '@/types/common/PageRequest';
import { getDefaultAvatar } from '@/utils/defaultImage';

@Component({
	selector: 'app-post-comment',
	templateUrl: './post-comment.component.html',
	styleUrl: './post-comment.component.css'
})
export class PostCommentComponent {
	@Input() postId: number | undefined = undefined;
	@Input() comment: Comment | null = null;

	getAvatarUrl(): string {
		return `${environment.baseApiUrl}/users/${this.comment?.authorId}/avatar`;
	}

	setDefaultImage(event: Event): void {
		const target = event.target as HTMLImageElement;
		target.src = getDefaultAvatar();
		target.alt = "Default Avatar";
	}
}
