import { AdminService } from 'src/app/services/admin.service';
import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { PageRequest } from '@/types/common/PageRequest';
import { Post } from '@/types/Posts';
import { PostsService } from '@services/post.service';
import { User } from '@/types/User';

@Injectable({
  providedIn: 'root'
})

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})


export class AdminComponent implements OnInit {
    currrentPageRequest: PageRequest = {
    page: 0,
    size: 4}
    posts: Post[] = [];
    users: User[] = [];
    isLoading = true;
    isLoadingUsers = true;
    isLastPagePosts = false;
    isLastPageUsers = false;
    error: string | null = null;

    constructor(private postsService: PostsService, private adminService: AdminService) {}

    fetchPosts(): void {
        this.isLoading = true;
        this.postsService.getPosts(this.currrentPageRequest).subscribe({
            next: (response) => {
                this.posts = this.posts.concat(response.data.page);
                this.isLastPagePosts = response.data.is_last_page;
                this.isLoading = false;
                this.currrentPageRequest.page += 1;
            },
            error: (error) => {
                this.isLoading = false;
                this.error = error.error.message;
                console.error('Error fetching posts:', error);
            }
        });
    }

    fetchUsers(): void {
        this.isLoadingUsers = true;
        this.adminService.getUsers(this.currrentPageRequest).subscribe({
          next: (response) => {
            this.users = this.users.concat(response.data.page);
            this.isLastPageUsers = response.data.is_last_page;
            this.isLoadingUsers = false;
            this.currrentPageRequest.page += 1;
          },
          error: (error) => {
            this.isLoadingUsers = false;
            this.error = error.error.message;
            console.error('Error fetching users:', error);
          }
        });
      }

    ngOnInit(): void {
        this.fetchPosts();
        this.fetchUsers();
    }

    deletePost(postId: number): void {
    }

    deleteUser(userId: number): void {

    }


}
