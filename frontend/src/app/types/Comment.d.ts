
export interface Comment {
    id: number;
    authorId: number;
    content: string;
    formatedDate: string;
    author: string;
};

export interface CreateCommentRequest {
    postId: number;
    content: string;
};