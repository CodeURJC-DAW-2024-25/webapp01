import { AuthService } from '@/services/auth.service';
import { CreatePostRequest } from '@/types/Posts';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { PostsService } from 'src/app/services/post.service';

@Component({
  selector: 'app-create-post-form',
  templateUrl: './create-post-form.component.html',
  styleUrls: ['./create-post-form.component.css']
})
export class CreatePostFormComponent implements OnInit {
  isAdmin: boolean = false;
  post: any;
  tags: string[] = ['Ahorro', 'Finanzas', 'Supermercado', 'Producto', 'Tip'];
  postForm!: FormGroup;
  selectedFile: File | null = null;
  bannerUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private postService: PostsService,
    private router: Router,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.isAdmin();

    this.postForm = this.fb.group({
      title: ['', Validators.required],
      visibility: ['PUBLIC'],
      description: [''],
      author: [''],
      tags: [['Ahorro']],
      content: [''],
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.postService.getPostById(+id).subscribe({
        next: (res) => {
          this.post = res.data;
          this.postForm.patchValue({
            title: this.post.title,
            visibility: this.post.visibility,
            description: this.post.description,
            author: this.post.author,
            tags: this.post.tags,
            content: this.post.content,
          });
          this.bannerUrl = `https://localhost:8443/api/v1/posts/${this.post.id}/banner?t=${Date.now()}`;
        },
        error: (err) => {
          console.error('Error loading the post:', err);
        },
      });
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  onSubmit(): void {
    if (!this.isAdmin || this.postForm.invalid) return;

    const formValues = this.postForm.value;

    const postRequest: CreatePostRequest = {
      title: formValues.title,
      description: formValues.description,
      content: formValues.content,
      author: formValues.author,
      date: new Date().toISOString(),
      visibility: formValues.visibility,
      tags: formValues.tags,
    };

    if (this.post) {
      const postId = this.post.id;
      this.postService.updatePost(postId, postRequest).subscribe({
        next: () => {
          if (this.selectedFile) {
            this.postService.uploadPostBanner(postId, this.selectedFile).subscribe({
              next: () => this.router.navigate(['/posts', postId]),
              error: (err) => {
                console.warn('Post updated, but error uploading image:', err);
                this.router.navigate(['/posts', postId]);
              }
            });
          } else {
            this.router.navigate(['/posts', postId]);
          }
        },
        error: (err) => {
          console.error('Error updating the post:', err);
        }
      });
    } else {
      this.postService.createPost(postRequest).subscribe({
        next: (response) => {
          const postId = response?.data?.id;

          if (this.selectedFile && postId) {
            this.postService.uploadPostBanner(postId, this.selectedFile).subscribe({
              next: () => {
                this.router.navigate(['/posts', postId]);
              },
              error: (err) => {
                console.warn('Post created, but error uploading image:', err);
                this.router.navigate(['/posts', postId]);
              },
            });
          } else {
            this.router.navigate(['/posts', postId]);
          }
        },
        error: (error) => {
          console.error('Error creating the post:', error);
        },
      });
    }
  }
}
