import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Post } from "@/types/Posts";
import { environment } from "@environments/environment";
import { PageRequest } from "@/types/common/PageRequest";
import { Response } from "@/types/common/Response";
import { PaginatedResponse } from "@/types/common/PaginatedResponse";

@Injectable({
    providedIn: "root"
})
export class PostsService {
    private apiUrl = `${environment.baseApiUrl}/posts`;

    constructor(private http: HttpClient) {}

    getPosts(req: PageRequest): Observable<PaginatedResponse<Post>> {
        const builtUrl = `${this.apiUrl}?page=${req.page}&size=${req.size}`;
        return this.http.get<PaginatedResponse<Post>>(builtUrl);
    }

    getPostById(id: string): Observable<Response<Post>> {
        const builtUrl = `${this.apiUrl}/${id}`;
        return this.http.get<Response<Post>>(builtUrl);
    }

    getPostContentById(id: string): Observable<string> {
        const builtUrl = `${this.apiUrl}/${id}/content`;
        return this.http.get(builtUrl, {
            responseType: "text"
        });
    }
}