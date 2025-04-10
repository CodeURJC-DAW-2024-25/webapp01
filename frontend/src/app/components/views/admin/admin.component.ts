import { UsersService } from '@/services/user.service';
import { Component, inject, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
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
	postsService = inject(PostsService);
	usersService = inject(UsersService);

	postsData = {
		currentPageReq: { page: 0, size: 4 } as PageRequest,
		posts: [] as Post[],
		isLoading: true,
		isLastPage: false,
		error: null as string | null
	}
	usersData = {
		currentPageReq: { page: 0, size: 4 } as PageRequest,
		users: [] as User[],
		isLoading: true,
		isLastPage: false,
		error: null as string | null
	}

	fetchPosts(): void {
		this.postsData.isLoading = true;
		this.postsService.getPosts(this.postsData.currentPageReq).subscribe({
			next: (response) => {
				this.postsData.posts = this.postsData.posts.concat(response.data.page);
				this.postsData.isLastPage = response.data.is_last_page;
				this.postsData.isLoading = false;
				this.postsData.currentPageReq.page += 1;
			},
			error: (error) => {
				this.postsData.isLoading = false;
				this.postsData.error = error.error.message;
				console.error('Error fetching posts:', error);
			}
		});
	}

	fetchUsers(): void {
		this.postsData.isLoading = true;
		this.usersService.getUsers(this.usersData.currentPageReq).subscribe({
			next: (response) => {
				this.usersData.users = this.usersData.users.concat(response.data.page);
				this.usersData.isLastPage = response.data.is_last_page;
				this.usersData.isLoading = false;
				this.usersData.currentPageReq.page += 1;
			},
			error: (error) => {
				this.usersData.isLoading = false;
				this.usersData.error = error.error.message;
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
