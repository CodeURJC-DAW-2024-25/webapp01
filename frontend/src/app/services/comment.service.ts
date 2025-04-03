import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { PageRequest } from "@/types/common/PageRequest";
import { PaginatedResponse } from "@/types/common/PaginatedResponse";
import { Comment } from "@/types/Comment";

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
}