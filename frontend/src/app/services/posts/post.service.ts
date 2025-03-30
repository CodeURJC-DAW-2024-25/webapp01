import { Injectable } from "@angular/core";
import { environment } from "../../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { PageRequest } from "../../types/common/PageRequest";
import { Post } from "../../types/Posts";
import { PaginatedResponse } from "../../types/common/PaginatedResponse";

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
}