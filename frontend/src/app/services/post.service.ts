import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { CreatePostRequest, Post } from "@/types/Posts";
import { environment } from "@environments/environment";
import { PageRequest } from "@/types/common/PageRequest";
import { Response } from "@/types/common/Response";
import { PaginatedResponse } from "@/types/common/PaginatedResponse";

@Injectable({
    providedIn: "root"
})
export class PostsService {
    private apiUrl = `${environment.baseApiUrl}/v1/posts`;

    constructor(private http: HttpClient) {}

    getPosts(req: PageRequest): Observable<PaginatedResponse<Post>> {
        const builtUrl = `${this.apiUrl}?page=${req.page}&size=${req.size}`;
        return this.http.get<PaginatedResponse<Post>>(builtUrl);
    }

    getPostById(id: number): Observable<Response<Post>> {
        const builtUrl = `${this.apiUrl}/${id}`;
        return this.http.get<Response<Post>>(builtUrl);
    }

    getPostContentById(id: number): Observable<string> {
        const builtUrl = `${this.apiUrl}/${id}/content`;
        return this.http.get(builtUrl, {
            responseType: "text"
        });
    }

    deletePost(id: number): Observable<Response<Post>> {
        const builtUrl = `${this.apiUrl}/${id}`;
        return this.http.delete<Response<Post>>(builtUrl, {
            withCredentials: true
        });
    }

    createPost(postData: CreatePostRequest): Observable<any> {
        return this.http.post(`${this.apiUrl}`, postData, {
          withCredentials: true,
          headers: {
            'Content-Type': 'application/json'
          }
        });
    }

    uploadPostBanner(id: number, file: File): Observable<any> {
        const formData = new FormData();
        formData.append('banner', file);
        return this.http.post(`${this.apiUrl}/${id}/banner`, formData, {
          withCredentials: true
        });
    }

    updatePost(id: number, postData: CreatePostRequest): Observable<any> {
        return this.http.patch(`${this.apiUrl}/${id}`, postData, {
          withCredentials: true,
          headers: {
            'Content-Type': 'application/json'
          }
        });
    }

}
