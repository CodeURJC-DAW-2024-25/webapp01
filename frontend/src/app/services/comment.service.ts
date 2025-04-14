import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { PageRequest } from "@/types/common/PageRequest";
import { PaginatedResponse } from "@/types/common/PaginatedResponse";
import { Comment, CreateCommentRequest, DeleteCommentRequest } from "@/types/Comment";
import { Response } from "@/types/common/Response";

@Injectable({
    providedIn: "root"
})
export class CommentsService {
    private apiUrl = `${environment.baseApiUrl}/v1/posts`;

    constructor(private http: HttpClient) {}

    getPostComments(postId: number, req: PageRequest): Observable<PaginatedResponse<Comment>> {
        const builtUrl = `${this.apiUrl}/${postId}/comments?page=${req.page}&size=${req.size}`;
        return this.http.get<PaginatedResponse<Comment>>(builtUrl);
    }

    createPostComment(commentRequest: CreateCommentRequest): Observable<Response<Comment>> {
        const builtUrl = `${this.apiUrl}/${commentRequest.postId}/comments`;
        return this.http.post<Response<Comment>>(builtUrl, commentRequest, {
            withCredentials: true,
        });
    }

    deletePostComment(commentRequest: DeleteCommentRequest) {
        const builtUrl = `${this.apiUrl}/${commentRequest.postId}/comments/${commentRequest.commentId}`;
        return this.http.delete<Response<Comment>>(builtUrl, {
            withCredentials: true,
        });
    }
}