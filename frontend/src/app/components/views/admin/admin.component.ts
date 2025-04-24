import { UsersService } from '@/services/user.service';
import { Component, inject, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { PageRequest } from '@/types/common/PageRequest';
import { Post } from '@/types/Posts';
import { PostsService } from '@services/post.service';
import { User } from '@/types/User';
import { loadGraphs } from '@/utils/loadGraphs';
import { StatsService } from '@/services/stats.service';

@Injectable({
    providedIn: 'root'
})

@Component({
    selector: 'app-admin',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.css']
})

export class AdminComponent implements OnInit {
    private postsService = inject(PostsService);
    private usersService = inject(UsersService);
    private statsService = inject(StatsService);

    postsData = {
        currentPageReq: { page: 0, size: 4 } as PageRequest,
        posts: [] as Post[],
        isLoading: true,
        isLastPage: false,
        error: null as string | null,
        total_items: 0 as number
    }
    usersData = {
        currentPageReq: { page: 0, size: 4 } as PageRequest,
        users: [] as User[],
        isLoading: true,
        isLastPage: false,
        error: null as string | null,
        total_items: 0 as number
    }

    userCurrentPage = 1;
    userPageSize = 5;
    paginatedUsers: User[] = [];
    userTotalPages: number[] = [];

    postCurrentPage = 1;
    postPageSize = 5;
    paginatedPosts: Post[] = [];
    postTotalPages: number[] = [];

    fetchPosts(page: number): void {
        this.postsData.isLoading = true;
        this.postsData.currentPageReq.page = page - 1; // Adjust for zero-based indexing
        this.postsService.getPosts(this.postsData.currentPageReq).subscribe({
            next: (response) => {
                this.postsData.posts = response.data.page;
                this.postsData.isLastPage = response.data.is_last_page;
                this.postsData.isLoading = false;
                this.postsData.total_items = response.data.total_items;
                this.postCurrentPage = page; // Update the current page
                this.paginatedPosts = response.data.page;
                this.updatePostPagination();
            },
            error: (error) => {
                this.postsData.isLoading = false;
                this.postsData.error = error.error.message;
                console.error('Error fetching posts:', error);
            }
        });
    }

    fetchUsers(page: number): void {
        this.usersData.isLoading = true;
        this.usersData.currentPageReq.page = page - 1; // Adjust for zero-based indexing
        this.usersService.getUsers(this.usersData.currentPageReq).subscribe({
            next: (response) => {
                this.usersData.users = response.data.page;
                this.usersData.isLastPage = response.data.is_last_page;
                this.usersData.isLoading = false;
                this.usersData.total_items = response.data.total_items;
                this.userCurrentPage = page; // Update the current page
                this.paginatedUsers = response.data.page;
                this.updateUserPagination();
            },
            error: (error) => {
                this.usersData.isLoading = false;
                this.usersData.error = error.error.message;
                console.error('Error fetching users:', error);
            }
        });
    }

    ngOnInit(): void {
        this.fetchPosts(this.postCurrentPage); // Fetch the first page of posts
        this.fetchUsers(1); // Fetch the first page of users
        this.loadScript('https://cdn.jsdelivr.net/npm/chart.js');
    }

    updateUserPagination(): void {
        const totalUsers = this.usersData.total_items;
        this.userTotalPages = Array(Math.ceil(totalUsers / this.userPageSize))
            .fill(0)
            .map((_, i) => i + 1);
            
    }

    updatePostPagination(): void {
        const totalPosts = this.postsData.total_items;
        this.postTotalPages = Array(Math.ceil(totalPosts / this.postPageSize))
            .fill(0)
            .map((_, i) => i + 1);
    }

    changeUserPage(page: number): void {
        if (page < 1 || page > this.userTotalPages.length) return;
        this.fetchUsers(page); // Fetch data for the new page
    }

    changePostPage(page: number): void {
        if (page < 1 || page > this.postTotalPages.length) return;
        this.fetchPosts(page); // Fetch data for the new page
    }

    loadScript(src: string) {
        const script = document.createElement('script');
        script.src = src;
        script.async = true;
        script.defer = true;
        script.type = 'text/javascript';
        document.head.appendChild(script);
        script.onload = () => {
            this.statsService.getProductsStats().subscribe({
                next: (response) => {
                    const data = response.data;
                    loadGraphs((window as any).Chart, data);
                },
                error: (error) => {
                    console.error('Error fetching stats:', error);
                }
            });
        }
    }

    deletePost(postId: number): void {
        this.postsService.deletePost(postId).subscribe({
            next: () => {
                this.postsData.posts = this.postsData.posts.filter(post => post.id !== postId);
                this.updatePostPagination();
            },
            error: (error) => {
                console.error('Error deleting post:', error);
            }
        });
    }

    deleteUser(userId: number): void {
        this.usersService.deleteUser(userId).subscribe({
            next: () => {
                this.usersData.users = this.usersData.users.filter(user => user.id !== userId);
                this.updateUserPagination();
            },
            error: (error) => {
                console.error('Error deleting user:', error);
            }
        });
    }
}
