import { Post } from "@/types/Posts";
import { User } from "@/types/User";
import { environment } from "@environments/environment";

export function getDefaultImage(): string {
    if (environment.production) return "/new/assets/images/template_image.png";
    return "/assets/images/template_image.png";
}

export function getDefaultAvatar(): string {
    if (environment.production) return "/new/assets/images/default_avatar.jpg";
    return "/assets/images/default_avatar.jpg";
}

export function getUserAvatar(user: User | null): string {
    const userId = user?.id;

    if (userId) {
        return `${environment.baseApiUrl}/v1/users/${userId}/avatar`;
    }

    return getDefaultAvatar();
}

export function getPostBanner(post: Post | null): string {
    const postId = post?.id;

    if (postId) {
        return `${environment.baseApiUrl}/v1/posts/${postId}/banner`;
    }

    return getDefaultImage();
}